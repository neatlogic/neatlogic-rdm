/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.module.rdm.api.issue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.IssueGroupSearch;
import neatlogic.framework.rdm.enums.IssueRelType;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.AppAttrNotFoundException;
import neatlogic.framework.rdm.exception.ProjectNotAuthIssueException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.service.IssueService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveIssueApi extends PrivateApiComponentBase {
    private static final String TESTCASE_APP_TYPE = "testcase";


    @Resource
    private AttrMapper attrMapper;
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private IssueService issueService;


    @Override
    public String getName() {
        return "nmrai.saveissueapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public JSONObject example() {
        JSONObject defaultJson = new JSONObject();
        defaultJson.put("appId", 1111111111111L);
        defaultJson.put("name", "任务标题");
        defaultJson.put("attrList", new JSONArray(
        ) {{
            this.add(new JSONObject() {{
                this.put("attrName", "appname1");
                this.put("valueList", new JSONArray() {{
                    this.add("value1");
                    this.add("value2");
                    this.add("value3");
                }});
            }});
            this.add(new JSONObject() {{
                this.put("attrName", "appname2");
                this.put("valueList", new JSONArray() {{
                    this.add("value4");
                    this.add("value5");
                    this.add("value6");
                }});
            }});
        }});
        defaultJson.put("userIdList", new JSONArray() {{
            this.add("admin");
        }});
        return defaultJson;
    }

    @Input({
            @Param(name = "id", type = ApiParamType.LONG, desc = "nmrai.saveissueapi.input.param.desc.id"),
            @Param(name = "fromId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.fromid"),
            @Param(name = "toId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.toid"),
            @Param(name = "relType", type = ApiParamType.ENUM, member = IssueRelType.class, desc = "common.reltype"),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "term.rdm.parenttaskid"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc", isRequired = true),
            @Param(name = "name", type = ApiParamType.STRING, isRequired = true, maxLength = 50, desc = "nmrai.saveissueapi.input.param.desc.name"),
            @Param(name = "priority", type = ApiParamType.LONG, desc = "common.priority"),
            @Param(name = "iteration", type = ApiParamType.LONG, desc = "common.iteration"),
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "common.catalog"),
            @Param(name = "tagList", type = ApiParamType.JSONARRAY, desc = "common.tag"),
            @Param(name = "status", type = ApiParamType.LONG, desc = "common.status"),
            @Param(name = "attrList", type = ApiParamType.JSONARRAY, desc = "nmrai.saveissueapi.input.param.desc.attrlist"),
            @Param(name = "userIdList", type = ApiParamType.JSONARRAY, desc = "common.userlist"),
            @Param(name = "comment", type = ApiParamType.STRING, desc = "common.comment")})
    @Output({@Param(name = "id", type = ApiParamType.LONG, desc = "term.rdm.issueid")})
    @ResubmitInterval
    @Description(desc = "nmrai.saveissueapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long appId = paramObj.getLong("appId");
        if (!ProjectAuthManager.checkAppAuth(appId, ProjectUserType.MEMBER, ProjectUserType.OWNER, ProjectUserType.LEADER)) {
            throw new ProjectNotAuthIssueException();
        }
        AppVo appVo = appMapper.getAppById(appId);
        IssueVo issueVo = JSON.toJavaObject(paramObj, IssueVo.class);
        if (CollectionUtils.isNotEmpty(issueVo.getAttrList())) {
            for (IssueAttrVo attr : issueVo.getAttrList()) {
                if (attr.getAttrId() == null && StringUtils.isNotBlank(attr.getAttrName())) {
                    Long attrId = attrMapper.getAttrIdByAppIdAndName(issueVo.getAppId(), attr.getAttrName());
                    if (attrId != null) {
                        attr.setAttrId(attrId);
                    } else {
                        throw new AppAttrNotFoundException(attr.getAttrName());
                    }
                }
            }
        }
        issueVo.setCreateUser(UserContext.get().getUserUuid(true));
        issueVo.formatAttr();
        Long id = paramObj.getLong("id");
        List<AppAttrVo> appAttrList = attrMapper.getAttrByAppId(issueVo.getAppId());
        //补充页面没有提供的自定义属性
        for (AppAttrVo appAttrVo : appAttrList) {
            if (appAttrVo.getIsPrivate().equals(0)) {
                if (issueVo.getAttr(appAttrVo.getId()) == null) {
                    issueVo.addAttr(new IssueAttrVo(appAttrVo.getId(), issueVo.getId(), appAttrVo.getType(), appAttrVo.getConfig()));
                } else {
                    issueVo.getAttr(appAttrVo.getId()).setAttrType(appAttrVo.getType()).setConfig(appAttrVo.getConfig());
                }
            }
        }

        //自动替换关系配置中的处理人
        if (issueVo.getStatus() != null) {
            Long oldIssueId = 0L;
            if (id != null) {
                oldIssueId = issueMapper.getIssueStatusById(id);
            }
            oldIssueId = oldIssueId == null ? 0L : oldIssueId;
            if (!issueVo.getStatus().equals(oldIssueId)) {
                AppStatusRelVo appStatusRelVo = new AppStatusRelVo();
                appStatusRelVo.setFromStatusId(oldIssueId);
                appStatusRelVo.setToStatusId(issueVo.getStatus());
                appStatusRelVo.setAppId(issueVo.getAppId());
                AppStatusRelVo rel = appMapper.getAppStatusRel(appStatusRelVo);
                if (rel != null && MapUtils.isNotEmpty(rel.getConfig()) && rel.getConfig().containsKey("userList")) {
                    List<String> userIdList = new ArrayList<>();
                    for (int i = 0; i < rel.getConfig().getJSONArray("userList").size(); i++) {
                        JSONObject userObj = rel.getConfig().getJSONArray("userList").getJSONObject(i);
                        userIdList.add(userObj.getString("value").replace(IssueGroupSearch.PROJECTUSERTYPE.getValue() + "#", ""));
                    }
                    issueVo.setUserIdList(userIdList);
                }
            }
        }

        Long fromId = issueVo.getFromId();
        Long toId = issueVo.getToId();
        String relType = StringUtils.isNotBlank(issueVo.getRelType()) ? issueVo.getRelType() : IssueRelType.EXTEND.getValue();
        boolean needTestcaseCopyRel = appVo != null && TESTCASE_APP_TYPE.equals(appVo.getType()) && (fromId != null || toId != null);
        if (needTestcaseCopyRel) {
            // 测试用例在需求/测试计划中新增时，先保存用例库原用例，再用副本承载上下文快照。
            issueVo.setFromId(null);
            issueVo.setToId(null);
            issueVo.setRelType(null);
        }
        issueService.saveIssue(issueVo);
        if (needTestcaseCopyRel) {
            IssueVo copyIssue = issueService.copyIssue(issueVo.getId());
            IssueRelVo issueRelVo = new IssueRelVo();
            issueRelVo.setRelType(relType);
            if (fromId != null) {
                IssueVo fromIssue = issueMapper.getIssueById(fromId);
                issueRelVo.setFromIssueId(fromIssue.getId());
                issueRelVo.setFromAppId(fromIssue.getAppId());
                issueRelVo.setToIssueId(copyIssue.getId());
                issueRelVo.setToAppId(copyIssue.getAppId());
            } else {
                IssueVo toIssue = issueMapper.getIssueById(toId);
                issueRelVo.setToIssueId(toIssue.getId());
                issueRelVo.setToAppId(toIssue.getAppId());
                issueRelVo.setFromIssueId(copyIssue.getId());
                issueRelVo.setFromAppId(copyIssue.getAppId());
            }
            issueMapper.insertIssueRel(issueRelVo);
        }
        return issueVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/issue/save";
    }

}
