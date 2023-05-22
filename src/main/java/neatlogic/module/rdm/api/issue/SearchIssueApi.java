/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.module.rdm.api.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.AppCatalogVo;
import neatlogic.framework.rdm.dto.IssueAttrVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.CatalogMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchIssueApi extends PrivateApiComponentBase {
    @Resource
    private AttrMapper attrMapper;
    @Resource
    private IssueMapper issueMapper;


    @Resource
    private CatalogMapper catalogMapper;

    @Override
    public String getName() {
        return "搜索任务";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, desc = "关键字"),
            @Param(name = "fromId", type = ApiParamType.LONG, desc = "来源任务id"),
            @Param(name = "toId", type = ApiParamType.LONG, desc = "目标任务id"),
            @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id"),
            @Param(name = "priority", type = ApiParamType.LONG, desc = "优先级"),
            @Param(name = "status", type = ApiParamType.LONG, desc = "状态"),
            @Param(name = "tagList", type = ApiParamType.JSONARRAY, desc = "标签"),
            @Param(name = "iteration", type = ApiParamType.LONG, desc = "迭代"),
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "目录"),
            @Param(name = "mode", type = ApiParamType.ENUM, desc = "显示模式", rule = "level,list"),
            @Param(name = "attrFilterList", type = ApiParamType.JSONARRAY, desc = "自定义属性列表"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "当前页"),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "每页大小")})
    @Output({
            @Param(explode = BasePageVo.class),
            @Param(name = "tbodyList", explode = IssueVo[].class)})
    @Description(desc = "搜索任务接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueVo issueVo = JSONObject.toJavaObject(paramObj, IssueVo.class);
        if (issueVo.getCatalog() != null) {
            AppCatalogVo catalogVo = catalogMapper.getAppCatalogById(issueVo.getCatalog());
            issueVo.setCatalogLft(catalogVo.getLft());
            issueVo.setCatalogRht(catalogVo.getRht());
        }
        List<AppAttrVo> attrList = attrMapper.getAttrByAppId(issueVo.getAppId());
        for (AppAttrVo attr : attrList) {
            if (attr.getIsPrivate().equals(0)) {
                issueVo.addAppAttr(attr);
            }
        }
        //补充条件中的属性类型，sql语句中需要用到
        if (CollectionUtils.isNotEmpty(issueVo.getAttrFilterList())) {
            Iterator<IssueAttrVo> it = issueVo.getAttrFilterList().iterator();
            while (it.hasNext()) {
                IssueAttrVo issueAttrVo = it.next();
                Optional<AppAttrVo> attr = attrList.stream().filter(d -> d.getId().equals(issueAttrVo.getAttrId())).findFirst();
                if (attr.isPresent()) {
                    issueAttrVo.setAttrType(attr.get().getType());
                } else {
                    it.remove();
                }
            }
        }
        int rowNum = issueMapper.searchIssueCount(issueVo);
        issueVo.setRowNum(rowNum);
        List<IssueVo> issueList = new ArrayList<>();
        if (rowNum > 0) {
            List<Long> idList = issueMapper.searchIssueId(issueVo);
            if (CollectionUtils.isNotEmpty(idList)) {
                issueVo.setIdList(idList);
                issueList = issueMapper.searchIssue(issueVo);
                List<HashMap<String, ?>> attrMapList = issueMapper.getAttrByIssueIdList(issueVo);
                for (HashMap<String, ?> attrMap : attrMapList) {
                    if (attrMap.containsKey("issueId")) {
                        Long issueId = (Long) attrMap.get("issueId");
                        Optional<IssueVo> op = issueList.stream().filter(d -> d.getId().equals(issueId)).findFirst();
                        if (op.isPresent()) {
                            IssueVo queryIssueVo = op.get();
                            for (String key : attrMap.keySet()) {
                                if (!key.equals("issueId")) {
                                    IssueAttrVo issueAttrVo = new IssueAttrVo();
                                    issueAttrVo.setIssueId(queryIssueVo.getId());
                                    issueAttrVo.setAttrId(Long.parseLong(key));
                                    if (attrMap.get(key).toString().startsWith("[") && attrMap.get(key).toString().endsWith("]")) {
                                        issueAttrVo.setValueList(JSONArray.parseArray(attrMap.get(key).toString()));
                                    } else {
                                        issueAttrVo.setValueList(new JSONArray() {{
                                            this.add(attrMap.get(key));
                                        }});
                                    }
                                    queryIssueVo.addAttr(issueAttrVo);
                                }
                            }

                        }
                    }
                }
            }
        }
        return TableResultUtil.getResult(issueList, issueVo);
    }

    @Override
    public String getToken() {
        return "/rdm/issue/search";
    }
}
