/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.module.rdm.api.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.IssueNotAuthSearchException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.CatalogMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
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
    private ProjectMapper projectMapper;
    @Resource
    private CatalogMapper catalogMapper;

    @Override
    public String getName() {
        return "nmrai.searchissueapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, maxLength = 50, desc = "common.keyword"),
            @Param(name = "fromId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.fromid"),
            @Param(name = "toId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.toid"),
            @Param(name = "projectId", type = ApiParamType.LONG, desc = "term.rdm.projectid"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc"),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "term.rdm.parenttaskid"),
            @Param(name = "priority", type = ApiParamType.LONG, desc = "common.priority"),
            @Param(name = "status", type = ApiParamType.JSONARRAY, desc = "common.status"),
            @Param(name = "tagList", type = ApiParamType.JSONARRAY, desc = "common.tag"),
            @Param(name = "iteration", type = ApiParamType.LONG, desc = "common.iteration"),
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "common.catalog"),
            @Param(name = "userIdList", type = ApiParamType.JSONARRAY, desc = "common.worker"),
            @Param(name = "isEnd", type = ApiParamType.INTEGER, rule = "0,1", desc = "common.isend"),
            @Param(name = "isMine", type = ApiParamType.INTEGER, rule = "0,1", desc = "term.rdm.ismytask"),
            @Param(name = "isMyCreated", type = ApiParamType.INTEGER, rule = "0,1", desc = "common.ismycreated"),
            @Param(name = "isProcessed", type = ApiParamType.INTEGER, rule = "0,1", desc = "term.rdm.isprocessed"),
            @Param(name = "isExpired", type = ApiParamType.INTEGER, rule = "0,1", desc = "common.isexpired"),
            @Param(name = "isFavorite", type = ApiParamType.INTEGER, rule = "0,1", desc = "nmrai.toggleissueisfavoriteapi.input.param.desc.isfavorite"),
            @Param(name = "mode", type = ApiParamType.ENUM, desc = "common.displaymode", rule = "level,list"),
            @Param(name = "sortList", type = ApiParamType.JSONARRAY, desc = "common.sort"),
            @Param(name = "attrFilterList", type = ApiParamType.JSONARRAY, desc = "common.customattribute"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "common.currentpage"),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "common.pagesize")})
    @Output({
            @Param(explode = BasePageVo.class),
            @Param(name = "tbodyList", explode = IssueVo[].class)})
    @Description(desc = "nmrai.searchissueapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        paramObj.put("maxPageSize", 500);//调整最大分页上限，为了应对故事墙搜索模式
        IssueConditionVo issueVo = JSONObject.toJavaObject(paramObj, IssueConditionVo.class);
        if (CollectionUtils.isNotEmpty(issueVo.getSortList())) {
            List<AppAttrVo> attrList = attrMapper.getAttrByAppId(issueVo.getAppId());

        }
        ProjectVo projectVo = projectMapper.getProjectById(issueVo.getProjectId());
        if (!ProjectAuthManager.checkProjectAuth(issueVo.getProjectId(), ProjectUserType.OWNER, ProjectUserType.LEADER, ProjectUserType.MEMBER)) {
            throw new IssueNotAuthSearchException();
        }
        if (issueVo.getIsMyCreated() != null && issueVo.getIsMyCreated().equals(1)) {
            issueVo.setCreateUser(UserContext.get().getUserUuid(true));
        }
        if (issueVo.getIsMine() != null && issueVo.getIsMine().equals(1)) {
            issueVo.setUserIdList(new ArrayList<String>() {{
                this.add(UserContext.get().getUserUuid(true));
            }});
        }
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
                issueList.forEach(issue -> {
                    issue.setIsProjectLeader(projectVo.getIsLeader());
                    issue.setIsProjectMember(projectVo.getIsMember());
                    issue.setIsProjectOwner(projectVo.getIsOwner());
                });
                if (issueVo.getAppId() != null) {
                    //提供具体的appid才需要补充自动属性
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
        }
        return TableResultUtil.getResult(issueList, issueVo);
    }

    @Override
    public String getToken() {
        return "/rdm/issue/search";
    }
}
