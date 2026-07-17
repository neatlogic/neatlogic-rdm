/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 *
 */

package neatlogic.module.rdm.notify.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.common.config.Config;
import neatlogic.framework.common.constvalue.Expression;
import neatlogic.framework.common.constvalue.FormHandlerType;
import neatlogic.framework.common.constvalue.ParamType;
import neatlogic.framework.common.dto.ValueTextVo;
import neatlogic.framework.dto.ConditionParamVo;
import neatlogic.framework.dto.ExpressionVo;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.notify.core.NotifyPolicyHandlerBase;
import neatlogic.framework.notify.dto.NotifyTriggerVo;
import neatlogic.framework.rdm.auth.label.PROJECT_MANAGE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.IssueAttrVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.IterationVo;
import neatlogic.framework.rdm.dto.PriorityVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.dto.ProjectUserVo;
import neatlogic.framework.rdm.dto.ProjectUserTypeVo;
import neatlogic.framework.rdm.enums.IssueGroupSearch;
import neatlogic.framework.rdm.enums.IssueUserType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.rdm.notify.constvalue.RdmNotifyParam;
import neatlogic.framework.rdm.notify.core.IRdmNotifyPolicyHandler;
import neatlogic.framework.rdm.notify.dto.RdmNotifyContextVo;
import neatlogic.framework.util.$;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.PriorityMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RDM通知策略处理器公共基类。
 * 设计方式与ITSM的ProcessTaskNotifyHandlerBase一致，公共能力由基类提供，具体分类仅追加触发点和参数。
 */
public abstract class RdmNotifyPolicyHandlerBase extends NotifyPolicyHandlerBase implements IRdmNotifyPolicyHandler {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private PriorityMapper priorityMapper;

    private static final List<RdmNotifyParam> COMMON_PARAM_LIST = Arrays.asList(
            RdmNotifyParam.BIZ_TYPE,
            RdmNotifyParam.PROJECT_ID,
            RdmNotifyParam.PROJECT_NAME,
            RdmNotifyParam.PROJECT_TYPE,
            RdmNotifyParam.PROJECT_START_DATE,
            RdmNotifyParam.PROJECT_END_DATE,
            RdmNotifyParam.PROJECT_URL,
            RdmNotifyParam.OPERATOR,
            RdmNotifyParam.CHANGED_FIELD_LIST,
            RdmNotifyParam.OLD_VALUE,
            RdmNotifyParam.NEW_VALUE
    );

    @Override
    protected List<NotifyTriggerVo> myNotifyTriggerList() {
        return myCustomNotifyTriggerList();
    }

    /**
     * 返回当前业务分类支持的触发点。
     */
    protected abstract List<NotifyTriggerVo> myCustomNotifyTriggerList();

    @Override
    protected List<ConditionParamVo> mySystemParamList() {
        List<ConditionParamVo> resultList = new ArrayList<>();
        for (RdmNotifyParam param : COMMON_PARAM_LIST) {
            resultList.add(createConditionParam(param));
        }
        List<RdmNotifyParam> customParamList = myCustomSystemParamList();
        if (CollectionUtils.isNotEmpty(customParamList)) {
            for (RdmNotifyParam param : customParamList) {
                resultList.add(createConditionParam(param));
            }
        }
        return resultList;
    }

    /**
     * 返回当前业务分类特有的模板参数。
     */
    protected List<RdmNotifyParam> myCustomSystemParamList() {
        return Collections.emptyList();
    }

    @Override
    protected List<ConditionParamVo> mySystemConditionOptionList() {
        List<ConditionParamVo> resultList = new ArrayList<>();
        for (ConditionParamVo source : mySystemParamList()) {
            ConditionParamVo param = new ConditionParamVo();
            param.setName(source.getName());
            param.setLabel(source.getLabel());
            param.setParamType(source.getParamType());
            param.setParamTypeName(source.getParamTypeName());
            param.setFreemarkerTemplate(source.getFreemarkerTemplate());
            param.setIsEditable(0);
            param.setType("common");
            param.setController("input");
            setConditionController(param);
            ParamType paramType = ParamType.getParamType(param.getParamType());
            if (paramType != null) {
                param.setDefaultExpression(paramType.getDefaultExpression().getExpression());
                for (Expression expression : paramType.getExpressionList()) {
                    param.getExpressionList().add(new ExpressionVo(expression.getExpression(), expression.getExpressionName(), expression.getIsShowConditionValue()));
                }
            }
            resultList.add(param);
        }
        return resultList;
    }

    /**
     * 为有限值条件补充下拉控件配置，普通文本、数字和日期参数继续沿用输入框。
     */
    private void setConditionController(ConditionParamVo param) {
        List<ValueTextVo> dataList = null;
        boolean multiple = false;
        boolean search = false;
        String paramName = param.getName();
        if (RdmNotifyParam.BIZ_TYPE.getValue().equals(paramName)
                || RdmNotifyParam.APP_TYPE.getValue().equals(paramName)) {
            dataList = getCurrentBizTypeOptionList();
        } else if (RdmNotifyParam.PROJECT_TYPE.getValue().equals(paramName)) {
            dataList = getProjectTypeOptionList();
            search = true;
        } else if (RdmNotifyParam.PROJECT_IS_CLOSE.getValue().equals(paramName)) {
            dataList = getProjectIsCloseOptionList();
        } else if (RdmNotifyParam.ITERATION_IS_OPEN.getValue().equals(paramName)) {
            dataList = getIterationIsOpenOptionList();
        } else if (RdmNotifyParam.STATUS_NAME.getValue().equals(paramName)
                || RdmNotifyParam.OLD_STATUS_NAME.getValue().equals(paramName)) {
            dataList = getStatusOptionList();
            search = true;
        } else if (RdmNotifyParam.PRIORITY_NAME.getValue().equals(paramName)) {
            dataList = getPriorityOptionList();
        } else if (RdmNotifyParam.CHANGED_FIELD_LIST.getValue().equals(paramName) && isIssueNotifyHandler()) {
            dataList = getChangedFieldOptionList();
            multiple = true;
        }
        if (dataList == null) {
            return;
        }
        param.setController(FormHandlerType.SELECT.toString());
        param.setConfig(buildSelectConfig(dataList, multiple, search));
    }

    /**
     * Issue型处理器覆盖此标识，使仅对Issue有意义的变更字段条件使用多选下拉。
     */
    protected boolean isIssueNotifyHandler() {
        return false;
    }

    /**
     * 生成framework条件编辑器识别的下拉控件配置。
     */
    private JSONObject buildSelectConfig(List<ValueTextVo> dataList, boolean multiple, boolean search) {
        JSONObject config = new JSONObject();
        config.put("type", FormHandlerType.SELECT.toString());
        config.put("search", search);
        config.put("multiple", multiple);
        config.put("isMultiple", multiple);
        config.put("value", "");
        if (multiple) {
            config.put("defaultValue", new JSONArray());
        } else {
            config.put("defaultValue", "");
        }
        config.put("dataList", dataList);
        return config;
    }

    /**
     * 业务分类和App类型只允许选择当前通知处理器对应的分类。
     */
    private List<ValueTextVo> getCurrentBizTypeOptionList() {
        List<ValueTextVo> dataList = new ArrayList<>();
        dataList.add(new ValueTextVo(getBizType(), getName()));
        return dataList;
    }

    /**
     * 返回项目模板及历史项目实际使用过的项目类型。
     */
    private List<ValueTextVo> getProjectTypeOptionList() {
        List<ValueTextVo> dataList = new ArrayList<>();
        List<String> projectTypeList = projectMapper.getProjectTypeList();
        if (CollectionUtils.isNotEmpty(projectTypeList)) {
            for (String projectType : projectTypeList) {
                dataList.add(new ValueTextVo(projectType, projectType));
            }
        }
        return dataList;
    }

    /**
     * 返回项目关闭状态的有限值选项。
     */
    private List<ValueTextVo> getProjectIsCloseOptionList() {
        List<ValueTextVo> dataList = new ArrayList<>();
        dataList.add(new ValueTextVo("0", $.t("term.rdm.notclosed")));
        dataList.add(new ValueTextVo("1", $.t("term.rdm.closed")));
        return dataList;
    }

    /**
     * 返回迭代开启状态的有限值选项。
     */
    private List<ValueTextVo> getIterationIsOpenOptionList() {
        List<ValueTextVo> dataList = new ArrayList<>();
        dataList.add(new ValueTextVo("0", $.t("term.rdm.notopened")));
        dataList.add(new ValueTextVo("1", $.t("term.rdm.opened")));
        return dataList;
    }

    /**
     * 按当前App类型返回去重后的状态标签。
     */
    private List<ValueTextVo> getStatusOptionList() {
        List<ValueTextVo> dataList = new ArrayList<>();
        List<String> statusLabelList = appMapper.getStatusLabelListByAppType(getBizType());
        if (CollectionUtils.isNotEmpty(statusLabelList)) {
            for (String statusLabel : statusLabelList) {
                dataList.add(new ValueTextVo(statusLabel, statusLabel));
            }
        }
        return dataList;
    }

    /**
     * 返回RDM全局优先级名称选项。
     */
    private List<ValueTextVo> getPriorityOptionList() {
        List<ValueTextVo> dataList = new ArrayList<>();
        List<PriorityVo> priorityList = priorityMapper.getPriorityList();
        if (CollectionUtils.isNotEmpty(priorityList)) {
            for (PriorityVo priorityVo : priorityList) {
                dataList.add(new ValueTextVo(priorityVo.getName(), priorityVo.getName()));
            }
        }
        return dataList;
    }

    /**
     * 返回Issue更新事件可参与条件判断的固定变更字段。
     */
    private List<ValueTextVo> getChangedFieldOptionList() {
        List<ValueTextVo> dataList = new ArrayList<>();
        dataList.add(new ValueTextVo("name", $.t("common.name")));
        dataList.add(new ValueTextVo("priority", $.t("common.priority")));
        dataList.add(new ValueTextVo("iteration", $.t("common.iteration")));
        dataList.add(new ValueTextVo("catalog", $.t("common.catalog")));
        dataList.add(new ValueTextVo("tagList", $.t("common.tag")));
        dataList.add(new ValueTextVo("startDate", $.t("common.startdate")));
        dataList.add(new ValueTextVo("endDate", $.t("common.enddate")));
        dataList.add(new ValueTextVo("timecost", $.t("term.rdm.plantimecost")));
        dataList.add(new ValueTextVo("content", $.t("common.content")));
        dataList.add(new ValueTextVo("userIdList", $.t("common.worker")));
        dataList.add(new ValueTextVo("attrList", $.t("common.customattribute")));
        return dataList;
    }

    @Override
    protected void myAuthorityConfig(JSONObject config) {
        List<String> groupList = JSON.parseArray(config.getJSONArray("groupList").toJSONString(), String.class);
        groupList.add(IssueGroupSearch.RDMUSERTYPE.getValue());
        config.put("groupList", groupList);
        List<String> includeList = JSON.parseArray(config.getJSONArray("includeList").toJSONString(), String.class);
        includeList.add(IssueGroupSearch.RDMUSERTYPE.getValue() + "#" + IssueUserType.COMMENTER.getValue());
        includeList.add(IssueGroupSearch.RDMUSERTYPE.getValue() + "#" + IssueUserType.REPLY_USER.getValue());
        config.put("includeList", includeList);
    }

    @Override
    public String getAuthName() {
        return PROJECT_MANAGE.class.getSimpleName();
    }

    @Override
    public String getModuleGroup() {
        return "rdm";
    }

    @Override
    public boolean isPublic() {
        if ("project".equals(getBizType())) {
            return true;
        }
        return AppTypeManager.isContain(getBizType());
    }

    @Override
    public JSONObject convertData(Object object, INotifyTriggerType notifyTriggerType) {
        JSONObject data = super.convertData(object, notifyTriggerType);
        if (!(object instanceof RdmNotifyContextVo)) {
            return data;
        }
        RdmNotifyContextVo context = (RdmNotifyContextVo) object;
        data.put(RdmNotifyParam.BIZ_TYPE.getValue(), context.getBizType());
        data.put(RdmNotifyParam.OPERATOR.getValue(), context.getOperatorName());
        data.put(RdmNotifyParam.CHANGED_FIELD_LIST.getValue(), context.getChangedFieldList());
        fillProjectData(data, context.getProjectVo());
        fillOldProjectData(data, context.getOldProjectVo());
        fillIterationData(data, context.getIterationVo(), context);
        fillIssueData(data, context.getIssueVo(), context.getOldIssueVo());
        if (context.getCommentVo() != null) {
            data.put(RdmNotifyParam.COMMENT_CONTENT.getValue(), context.getCommentVo().getContent());
        }
        data.put(RdmNotifyParam.OLD_VALUE.getValue(), buildSummary(context.getOldProjectVo(), context.getOldIterationVo(), context.getOldIssueVo()));
        data.put(RdmNotifyParam.NEW_VALUE.getValue(), buildSummary(context.getProjectVo(), context.getIterationVo(), context.getIssueVo()));
        return data;
    }

    private void fillProjectData(JSONObject data, ProjectVo projectVo) {
        if (projectVo == null) {
            return;
        }
        data.put(RdmNotifyParam.PROJECT_ID.getValue(), projectVo.getId());
        data.put(RdmNotifyParam.PROJECT_NAME.getValue(), projectVo.getName());
        data.put(RdmNotifyParam.PROJECT_DESCRIPTION.getValue(), projectVo.getDescription());
        data.put(RdmNotifyParam.PROJECT_TYPE.getValue(), projectVo.getType());
        data.put(RdmNotifyParam.PROJECT_START_DATE.getValue(), formatDate(projectVo.getStartDate()));
        data.put(RdmNotifyParam.PROJECT_END_DATE.getValue(), formatDate(projectVo.getEndDate()));
        data.put(RdmNotifyParam.PROJECT_URL.getValue(), buildUrl("/rdm.html#/project/" + projectVo.getId()));
        data.put(RdmNotifyParam.PROJECT_CONFIG_URL.getValue(), buildUrl("/rdm.html#/project-edit/" + projectVo.getId() + "?notifyTargetType=project&notifyTargetId=" + projectVo.getId()));
        data.put(RdmNotifyParam.PROJECT_MEMBER_SUMMARY.getValue(), buildMemberSummary(projectVo));
        data.put(RdmNotifyParam.PROJECT_IS_CLOSE.getValue(), projectVo.getIsClose());
    }

    private void fillOldProjectData(JSONObject data, ProjectVo projectVo) {
        if (projectVo == null) {
            return;
        }
        data.put(RdmNotifyParam.OLD_PROJECT_NAME.getValue(), projectVo.getName());
        data.put(RdmNotifyParam.OLD_PROJECT_DESCRIPTION.getValue(), projectVo.getDescription());
        data.put(RdmNotifyParam.OLD_PROJECT_START_DATE.getValue(), formatDate(projectVo.getStartDate()));
        data.put(RdmNotifyParam.OLD_PROJECT_END_DATE.getValue(), formatDate(projectVo.getEndDate()));
        data.put(RdmNotifyParam.OLD_PROJECT_MEMBER_SUMMARY.getValue(), buildMemberSummary(projectVo));
    }

    private List<String> buildMemberSummary(ProjectVo projectVo) {
        List<String> resultList = new ArrayList<>();
        if (projectVo == null || CollectionUtils.isEmpty(projectVo.getUserList())) {
            return resultList;
        }
        for (ProjectUserVo userVo : projectVo.getUserList()) {
            List<String> userTypeList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(userVo.getUserTypeList())) {
                for (ProjectUserTypeVo userTypeVo : userVo.getUserTypeList()) {
                    userTypeList.add(userTypeVo.getUserType());
                }
            }
            String userName = userVo.getUserName();
            if (StringUtils.isBlank(userName)) {
                userName = userVo.getUserId();
            }
            resultList.add(userName + "(" + String.join(",", userTypeList) + ")");
        }
        return resultList;
    }

    private void fillIterationData(JSONObject data, IterationVo iterationVo, RdmNotifyContextVo context) {
        if (iterationVo == null) {
            return;
        }
        data.put(RdmNotifyParam.ITERATION_ID.getValue(), iterationVo.getId());
        data.put(RdmNotifyParam.ITERATION_NAME.getValue(), iterationVo.getName());
        data.put(RdmNotifyParam.ITERATION_DESCRIPTION.getValue(), iterationVo.getDescription());
        data.put(RdmNotifyParam.ITERATION_START_DATE.getValue(), formatDate(iterationVo.getStartDate()));
        data.put(RdmNotifyParam.ITERATION_END_DATE.getValue(), formatDate(iterationVo.getEndDate()));
        data.put(RdmNotifyParam.ITERATION_IS_OPEN.getValue(), iterationVo.getIsOpen());
        data.put(RdmNotifyParam.ITERATION_ISSUE_COUNT.getValue(), iterationVo.getIssueCount());
        data.put(RdmNotifyParam.ITERATION_DONE_ISSUE_COUNT.getValue(), iterationVo.getDoneIssueCount());
        int completeRate = 0;
        if (iterationVo.getIssueCount() > 0) {
            completeRate = iterationVo.getDoneIssueCount() * 100 / iterationVo.getIssueCount();
        }
        data.put(RdmNotifyParam.ITERATION_COMPLETE_RATE.getValue(), completeRate);
        Long appId = 0L;
        if (context.getAppVo() != null) {
            appId = context.getAppVo().getId();
        }
        data.put(RdmNotifyParam.ITERATION_URL.getValue(), buildUrl("/rdm.html#/iteration-detail/" + iterationVo.getProjectId() + "/" + appId + "/" + iterationVo.getId()));
    }

    private void fillIssueData(JSONObject data, IssueVo issueVo, IssueVo oldIssueVo) {
        if (issueVo == null) {
            return;
        }
        data.put(RdmNotifyParam.ISSUE_ID.getValue(), issueVo.getId());
        data.put(RdmNotifyParam.ISSUE_NAME.getValue(), issueVo.getName());
        data.put(RdmNotifyParam.APP_TYPE.getValue(), issueVo.getAppType());
        data.put(RdmNotifyParam.APP_NAME.getValue(), issueVo.getAppName());
        data.put(RdmNotifyParam.STATUS_NAME.getValue(), issueVo.getStatusLabel());
        data.put(RdmNotifyParam.WORKER_LIST.getValue(), getUserNameList(issueVo));
        data.put(RdmNotifyParam.CREATOR.getValue(), issueVo.getCreateUserName());
        data.put(RdmNotifyParam.PRIORITY_NAME.getValue(), issueVo.getPriorityName());
        data.put(RdmNotifyParam.ISSUE_CONTENT.getValue(), issueVo.getContent());
        data.put(RdmNotifyParam.ISSUE_URL.getValue(), buildUrl("/rdm.html#/" + issueVo.getAppType() + "-detail/" + issueVo.getProjectId() + "/" + issueVo.getAppId() + "/" + issueVo.getId()));
        fillIssueAttrData(data, issueVo);
        if (oldIssueVo != null) {
            data.put(RdmNotifyParam.OLD_STATUS_NAME.getValue(), oldIssueVo.getStatusLabel());
            data.put(RdmNotifyParam.OLD_WORKER_LIST.getValue(), getUserNameList(oldIssueVo));
        }
    }

    /**
     * 以属性UUID作为稳定映射标识，把当前Issue的自定义属性值注入通知模板上下文。
     */
    private void fillIssueAttrData(JSONObject data, IssueVo issueVo) {
        if (CollectionUtils.isEmpty(issueVo.getAppAttrList())) {
            return;
        }
        for (AppAttrVo appAttrVo : issueVo.getAppAttrList()) {
            IssueAttrVo issueAttrVo = issueVo.getAttr(appAttrVo.getId());
            String value = "";
            if (issueAttrVo != null) {
                String attrValue = issueAttrVo.getValue();
                if (attrValue != null) {
                    value = attrValue;
                }
            }
            data.put(appAttrVo.getUuid(), value);
        }
    }

    private List<String> getUserNameList(IssueVo issueVo) {
        if (CollectionUtils.isNotEmpty(issueVo.getUserList())) {
            return issueVo.getUserList().stream().map(UserVo::getUserName).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(issueVo.getUserIdList())) {
            return new ArrayList<>(issueVo.getUserIdList());
        }
        return new ArrayList<>();
    }

    private String buildSummary(ProjectVo projectVo, IterationVo iterationVo, IssueVo issueVo) {
        if (issueVo != null) {
            return issueVo.getName();
        }
        if (iterationVo != null) {
            return iterationVo.getName();
        }
        if (projectVo != null) {
            return projectVo.getName();
        }
        return "";
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private String buildUrl(String path) {
        String homeUrl = Config.HOME_URL();
        if (StringUtils.isBlank(homeUrl)) {
            return "";
        }
        if (!homeUrl.endsWith("/")) {
            homeUrl += "/";
        }
        return homeUrl + TenantContext.get().getTenantUuid() + path;
    }
}
