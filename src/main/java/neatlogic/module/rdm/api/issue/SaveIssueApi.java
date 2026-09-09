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

import neatlogic.framework.restful.dto.ApiExampleVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.exception.type.ParamIrregularException;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.IssueGroupSearch;
import neatlogic.framework.rdm.enums.IssueRelType;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.AppAttrNotFoundException;
import neatlogic.framework.rdm.exception.IssueNotFoundException;
import neatlogic.framework.rdm.exception.ProjectNotAuthIssueException;
import neatlogic.framework.rdm.notify.constvalue.RdmIssueNotifyTriggerType;
import neatlogic.framework.rdm.notify.dto.RdmNotifyContextVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.service.IssueRelStrategyService;
import neatlogic.module.rdm.service.IssueService;
import neatlogic.module.rdm.notify.service.RdmNotifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveIssueApi extends PrivateApiComponentBase {
    @Resource
    private AttrMapper attrMapper;
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private IssueService issueService;
    @Resource
    private IssueRelStrategyService issueRelStrategyService;

    @Resource
    private RdmNotifyService rdmNotifyService;


    @Override
    public String getName() {
        return "nmrai.saveissueapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    /** 通过代码组装请求示例，返回带标题和说明的场景列表。 */
    @Override
    public java.util.List<ApiExampleVo> example() {
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
        return java.util.Collections.singletonList(new ApiExampleVo("common.example", "nf.api.example.replacevalues", defaultJson));
    }

    @Input({
            @Param(name = "id", type = ApiParamType.LONG, desc = "nmrai.saveissueapi.input.param.desc.id"),
            @Param(name = "fromId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.fromid"),
            @Param(name = "toId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.toid"),
            @Param(name = "relType", type = ApiParamType.ENUM, member = IssueRelType.class, desc = "common.reltype"),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "term.rdm.parenttaskid"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc", isRequired = true),
            @Param(name = "name", type = ApiParamType.STRING, maxLength = 50, desc = "nmrai.saveissueapi.input.param.desc.name"),
            @Param(name = "priority", type = ApiParamType.LONG, desc = "common.priority"),
            @Param(name = "iteration", type = ApiParamType.LONG, desc = "common.iteration"),
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "common.catalog"),
            @Param(name = "tagList", type = ApiParamType.JSONARRAY, desc = "common.tag"),
            @Param(name = "status", type = ApiParamType.LONG, desc = "common.status"),
            @Param(name = "startDate", type = ApiParamType.STRING, desc = "term.rdm.startdate"),
            @Param(name = "endDate", type = ApiParamType.STRING, desc = "term.rdm.enddate"),
            @Param(name = "timecost", type = ApiParamType.INTEGER, desc = "term.rdm.plantimecost"),
            @Param(name = "content", type = ApiParamType.STRING, desc = "common.content"),
            @Param(name = "attrList", type = ApiParamType.JSONARRAY, desc = "nmrai.saveissueapi.input.param.desc.attrlist"),
            @Param(name = "userIdList", type = ApiParamType.JSONARRAY, desc = "common.userlist"),
            @Param(name = "copyIssueIdList", type = ApiParamType.JSONARRAY, desc = "副本issue id列表"),
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
        issueVo.setSubmittedFieldList(getSubmittedFieldList(paramObj));
        prepareAttr(issueVo);
        issueVo.setCreateUser(UserContext.get().getUserUuid(true));
        issueVo.formatAttr();
        Long id = paramObj.getLong("id");
        IssueVo oldIssue = null;
        if (id != null) {
            oldIssue = issueService.getIssueById(id);
            if (oldIssue == null) {
                throw new IssueNotFoundException(id);
            }
            mergeMissingIssueField(paramObj, issueVo, oldIssue);
        } else if (StringUtils.isBlank(issueVo.getName())) {
            throw new ParamIrregularException("name");
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
        IssueVo fromIssue = null;
        IssueVo toIssue = null;
        boolean needCopyRel = false;
        if (fromId != null) {
            fromIssue = issueMapper.getIssueById(fromId);
            needCopyRel = fromIssue != null && issueRelStrategyService.needCopy(fromIssue.getAppId(), issueVo.getAppId(), relType);
        } else if (toId != null) {
            toIssue = issueMapper.getIssueById(toId);
            needCopyRel = toIssue != null && issueRelStrategyService.needCopy(issueVo.getAppId(), toIssue.getAppId(), relType);
        }
        if (needCopyRel) {
            // 开启副本能力的应用在上下文中新增时，先保存原件，再用副本承载上下文快照。
            issueVo.setFromId(null);
            issueVo.setToId(null);
            issueVo.setRelType(null);
        }
        issueVo.setCopyIssueIdList(getCopyIssueIdList(paramObj));
        issueService.saveIssue(issueVo);
        if (needCopyRel) {
            IssueVo copyIssue = issueService.copyIssue(issueVo.getId());
            IssueRelVo issueRelVo = new IssueRelVo();
            issueRelVo.setRelType(relType);
            if (fromId != null) {
                issueRelVo.setFromIssueId(fromIssue.getId());
                issueRelVo.setFromAppId(fromIssue.getAppId());
                issueRelVo.setToIssueId(copyIssue.getId());
                issueRelVo.setToAppId(copyIssue.getAppId());
            } else {
                issueRelVo.setToIssueId(toIssue.getId());
                issueRelVo.setToAppId(toIssue.getAppId());
                issueRelVo.setFromIssueId(copyIssue.getId());
                issueRelVo.setFromAppId(copyIssue.getAppId());
            }
            issueMapper.insertIssueRel(issueRelVo);
        }
        IssueVo currentIssueVo = issueService.getIssueById(issueVo.getId());
        RdmNotifyContextVo notifyContextVo = new RdmNotifyContextVo();
        notifyContextVo.setBizType(appVo.getType());
        notifyContextVo.setAppVo(appVo);
        notifyContextVo.setIssueVo(currentIssueVo);
        notifyContextVo.setOldIssueVo(oldIssue);
        notifyContextVo.setChangedFieldList(getSubmittedFieldList(paramObj));
        if (oldIssue == null) {
            rdmNotifyService.notify(notifyContextVo, RdmIssueNotifyTriggerType.CREATED);
        } else {
            if (paramObj.containsKey("status") && !Objects.equals(oldIssue.getStatus(), currentIssueVo.getStatus())) {
                rdmNotifyService.notify(notifyContextVo, RdmIssueNotifyTriggerType.STATUS_CHANGED);
            }
            if (!getWorkerSet(oldIssue).equals(getWorkerSet(currentIssueVo))) {
                rdmNotifyService.notify(notifyContextVo, RdmIssueNotifyTriggerType.WORKER_CHANGED);
            }
            if (hasOrdinaryUpdate(paramObj)) {
                rdmNotifyService.notify(notifyContextVo, RdmIssueNotifyTriggerType.UPDATED);
            }
        }
        if (StringUtils.isNotBlank(issueVo.getComment())) {
            CommentVo commentVo = new CommentVo();
            commentVo.setIssueId(issueVo.getId());
            commentVo.setContent(issueVo.getComment());
            commentVo.setFcu(UserContext.get().getUserUuid(true));
            notifyContextVo.setCommentVo(commentVo);
            rdmNotifyService.notify(notifyContextVo, RdmIssueNotifyTriggerType.COMMENTED);
        }
        return issueVo.getId();
    }

    /**
     * 状态、处理人和评论使用独立触发点，普通更新只覆盖其余业务字段。
     */
    private boolean hasOrdinaryUpdate(JSONObject paramObj) {
        String[] fieldArray = new String[]{"name", "priority", "iteration", "catalog", "tagList", "startDate", "endDate", "timecost", "content", "attrList"};
        for (String field : fieldArray) {
            if (paramObj.containsKey(field)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> getWorkerSet(IssueVo issueVo) {
        Set<String> resultSet = new HashSet<>();
        if (issueVo != null && CollectionUtils.isNotEmpty(issueVo.getUserIdList())) {
            resultSet.addAll(issueVo.getUserIdList());
        }
        return resultSet;
    }

    private void prepareAttr(IssueVo issueVo) {
        if (CollectionUtils.isEmpty(issueVo.getAttrList())) {
            return;
        }
        for (IssueAttrVo attr : issueVo.getAttrList()) {
            if (attr.getAttrId() == null && StringUtils.isNotBlank(attr.getAttrName())) {
                Long attrId = attrMapper.getAttrIdByAppIdAndName(issueVo.getAppId(), attr.getAttrName());
                if (attrId != null) {
                    attr.setAttrId(attrId);
                } else {
                    throw new AppAttrNotFoundException(attr.getAttrName());
                }
            }
            AppAttrVo appAttrVo = attrMapper.getAttrById(attr.getAttrId());
            if (appAttrVo == null || !issueVo.getAppId().equals(appAttrVo.getAppId())) {
                throw new AppAttrNotFoundException(attr.getAttrId());
            }
            attr.setAttrType(appAttrVo.getType()).setConfig(appAttrVo.getConfig());
        }
    }

    private void mergeMissingIssueField(JSONObject paramObj, IssueVo issueVo, IssueVo oldIssue) {
        if (oldIssue == null) {
            return;
        }
        if (!paramObj.containsKey("name")) {
            issueVo.setName(oldIssue.getName());
        }
        if (!paramObj.containsKey("priority")) {
            issueVo.setPriority(oldIssue.getPriority());
        }
        if (!paramObj.containsKey("iteration")) {
            issueVo.setIteration(oldIssue.getIteration());
        }
        if (!paramObj.containsKey("status")) {
            issueVo.setStatus(oldIssue.getStatus());
        }
        if (!paramObj.containsKey("catalog")) {
            issueVo.setCatalog(oldIssue.getCatalog());
        }
        if (!paramObj.containsKey("startDate")) {
            issueVo.setStartDate(oldIssue.getStartDate());
        }
        if (!paramObj.containsKey("endDate")) {
            issueVo.setEndDate(oldIssue.getEndDate());
        }
        if (!paramObj.containsKey("content")) {
            issueVo.setContent(oldIssue.getContent());
        }
        if (!paramObj.containsKey("timecost")) {
            issueVo.setTimecost(oldIssue.getTimecost());
        }
    }

    private List<String> getSubmittedFieldList(JSONObject paramObj) {
        List<String> submittedFieldList = new ArrayList<>();
        String[] fieldArray = new String[]{"name", "priority", "iteration", "catalog", "tagList", "startDate", "endDate", "timecost", "content", "userIdList", "attrList"};
        for (String field : fieldArray) {
            if (paramObj.containsKey(field)) {
                submittedFieldList.add(field);
            }
        }
        return submittedFieldList;
    }

    private List<Long> getCopyIssueIdList(JSONObject paramObj) {
        JSONArray copyIssueIdArray = paramObj.getJSONArray("copyIssueIdList");
        if (CollectionUtils.isNotEmpty(copyIssueIdArray)) {
            List<Long> copyIssueIdList = copyIssueIdArray.toJavaList(Long.class);
            for (Long copyIssueId : copyIssueIdList) {
                if (copyIssueId == null || copyIssueId <= 0) {
                    throw new ParamIrregularException("copyIssueIdList");
                }
            }
            return copyIssueIdList;
        }
        return new ArrayList<>();
    }

    @Override
    public String getToken() {
        return "/rdm/issue/save";
    }

}
