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
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchIssueApi extends PrivateApiComponentBase {

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "搜索任务";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, desc = "关键字"),
            @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "当前页"),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "每页大小"),
            @Param(name = "priority", type = ApiParamType.LONG, desc = "优先级"),
            @Param(name = "status", type = ApiParamType.LONG, desc = "状态"),
            @Param(name = "tagList", type = ApiParamType.JSONARRAY, desc = "标签"),
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "目录")})
    @Output({@Param(explode = IssueCountVo[].class)})
    @Description(desc = "搜索任务接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueVo issueVo = JSONObject.toJavaObject(paramObj, IssueVo.class);
        if (issueVo.getCatalog() != null) {
            AppCatalogVo catalogVo = appMapper.getAppCatalogById(issueVo.getCatalog());
            issueVo.setCatalogLft(catalogVo.getLft());
            issueVo.setCatalogRht(catalogVo.getRht());
        }
        List<AppAttrVo> attrList = appMapper.getAttrByAppId(issueVo.getAppId());
        for (AppAttrVo attr : attrList) {
            if (attr.getIsPrivate().equals(0)) {
                issueVo.addAttr(new IssueAttrVo(attr.getId(), attr.getType()));
            }
        }
        int rowNum = issueMapper.searchIssueCount(issueVo);
        issueVo.setRowNum(rowNum);
        List<IssueVo> issueList = new ArrayList<>();
        if (rowNum > 0) {
            issueList = issueMapper.searchIssue(issueVo);
            List<Long> idList = new ArrayList<>();
            for (IssueVo issue : issueList) {
                idList.add(issue.getId());
            }
            issueVo.setIdList(idList);
            List<HashMap<String, ?>> attrMapList = issueMapper.getAttrByIssueIdList(issueVo);
            for (HashMap<String, ?> attrMap : attrMapList) {
                if (attrMap.containsKey("issueId")) {
                    Long issueId = (Long) attrMap.get("issueId");
                    Optional<IssueVo> op = issueList.stream().filter(d -> d.getId().equals(issueId)).findFirst();
                    if (op.isPresent()) {
                        IssueVo queryIssueVo = op.get();
                        IssueAttrVo issueAttrVo = new IssueAttrVo();
                        issueAttrVo.setIssueId(queryIssueVo.getId());
                        for (String key : attrMap.keySet()) {
                            if (!key.equals("issueId")) {
                                issueAttrVo.setAttrId(Long.parseLong(key));
                                if (attrMap.get(key).toString().startsWith("[") && attrMap.get(key).toString().endsWith("]")) {
                                    issueAttrVo.setValueList(JSONArray.parseArray(attrMap.get(key).toString()));
                                } else {
                                    issueAttrVo.setValueList(new JSONArray() {{
                                        this.add(attrMap.get(key));
                                    }});
                                }
                            }
                        }
                        queryIssueVo.addAttr(issueAttrVo);
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
