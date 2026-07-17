/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.service;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.thread.NeatLogicThread;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.notify.dao.mapper.NotifyMapper;
import neatlogic.framework.notify.dto.InvokeNotifyPolicyConfigVo;
import neatlogic.framework.notify.dto.NotifyPolicyVo;
import neatlogic.framework.notify.dto.NotifyReceiverVo;
import neatlogic.framework.notify.dto.ParamMappingVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.ProjectUserTypeVo;
import neatlogic.framework.rdm.dto.ProjectUserVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.enums.AppType;
import neatlogic.framework.rdm.enums.IssueUserType;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.notify.core.IRdmNotifyPolicyHandler;
import neatlogic.framework.rdm.notify.dto.RdmNotifyContextVo;
import neatlogic.framework.transaction.core.AfterTransactionJob;
import neatlogic.framework.util.NotifyPolicyUtil;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.message.handler.RdmMessageHandler;
import neatlogic.module.rdm.notify.core.RdmNotifyPolicyHandlerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 解析RDM分层覆盖配置，并在事务提交后调用framework通知引擎。
 */
@Service
public class RdmNotifyServiceImpl implements RdmNotifyService {

    private static final Logger logger = LoggerFactory.getLogger(RdmNotifyServiceImpl.class);
    private static final String NOTIFY_POLICY_CONFIG = "notifyPolicyConfig";

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private NotifyMapper notifyMapper;

    @Override
    public void notify(RdmNotifyContextVo contextVo, INotifyTriggerType triggerType) {
        if (contextVo == null || triggerType == null || StringUtils.isBlank(contextVo.getBizType())) {
            return;
        }
        fillOperator(contextVo);
        AfterTransactionJob<Object> job = new AfterTransactionJob<>("RDM-NOTIFY-TRIGGER");
        job.execute(new NeatLogicThread("RDM-NOTIFY-" + triggerType.getTrigger()) {
            @Override
            protected void execute() {
                doNotify(contextVo, triggerType);
            }
        });
    }

    private void doNotify(RdmNotifyContextVo contextVo, INotifyTriggerType triggerType) {
        try {
            IRdmNotifyPolicyHandler handler = RdmNotifyPolicyHandlerFactory.getHandler(contextVo.getBizType());
            if (handler == null) {
                logger.warn("RDM通知停止：业务分类{}没有可用处理器", contextVo.getBizType());
                return;
            }
            enrichContext(contextVo);
            InvokeNotifyPolicyConfigVo configVo = getNotifyPolicyConfig(contextVo, handler);
            if (configVo == null) {
                logger.warn("RDM通知停止：无法读取{}的覆盖配置", contextVo.getBizType());
                return;
            }
            if (CollectionUtils.isNotEmpty(configVo.getExcludeTriggerList()) && configVo.getExcludeTriggerList().contains(triggerType.getTrigger())) {
                return;
            }
            NotifyPolicyVo policyVo = resolvePolicy(configVo, handler);
            if (policyVo == null) {
                logger.warn("RDM通知停止：分类{}、触发点{}没有有效策略，isCustom={}", contextVo.getBizType(), triggerType.getTrigger(), configVo.getIsCustom());
                LoggerFactory.getLogger("notifyAudit").info("RDM通知停止：分类={}，触发点={}，isCustom={}，原因=没有有效策略或自定义策略已失效",
                        contextVo.getBizType(), triggerType.getTrigger(), configVo.getIsCustom());
                return;
            }
            JSONObject conditionData = handler.convertData(contextVo, triggerType);
            applyAttrParamMapping(conditionData, configVo.getParamMappingList());
            String audit = "RDM分类=" + contextVo.getBizType() + ", 触发点=" + triggerType.getTrigger() + ", 策略=" + policyVo.getId();
            NotifyPolicyUtil.execute(policyVo.getHandler(), triggerType, RdmMessageHandler.class, policyVo,
                    configVo.getParamMappingList(), conditionData, buildReceiverMap(contextVo), contextVo, null, audit);
        } catch (Exception e) {
            // 通知异常只记录日志，绝不反向影响项目、迭代或Issue业务事务。
            logger.error("RDM通知执行失败，分类：{}，触发点：{}", contextVo.getBizType(), triggerType.getTrigger(), e);
        }
    }

    /**
     * 把属性映射同步写入条件参数，使策略中的自定义参数既能用于模板，也能用于通知条件。
     */
    private void applyAttrParamMapping(JSONObject conditionData, List<ParamMappingVo> paramMappingList) {
        if (conditionData == null || CollectionUtils.isEmpty(paramMappingList)) {
            return;
        }
        for (ParamMappingVo paramMappingVo : paramMappingList) {
            if (!Objects.equals("attr", paramMappingVo.getType())) {
                continue;
            }
            Object value = conditionData.get(paramMappingVo.getValue());
            if (value != null) {
                conditionData.put(paramMappingVo.getName(), value);
            }
        }
    }

    private NotifyPolicyVo resolvePolicy(InvokeNotifyPolicyConfigVo configVo, IRdmNotifyPolicyHandler handler) {
        if (configVo.getIsCustom() == 1) {
            if (configVo.getPolicyId() == null) {
                return null;
            }
            NotifyPolicyVo policyVo = notifyMapper.getNotifyPolicyById(configVo.getPolicyId());
            if (policyVo == null || !Objects.equals(policyVo.getHandler(), handler.getClassName())) {
                // 自定义策略失效时明确停止，不回退全局默认策略。
                return null;
            }
            return policyVo;
        }
        return notifyMapper.getDefaultNotifyPolicyByHandler(handler.getClassName());
    }

    private InvokeNotifyPolicyConfigVo getNotifyPolicyConfig(RdmNotifyContextVo contextVo, IRdmNotifyPolicyHandler handler) {
        InvokeNotifyPolicyConfigVo snapshot = contextVo.getNotifyPolicyConfig();
        if (snapshot != null) {
            snapshot.setHandler(handler.getClassName());
            return snapshot;
        }
        JSONObject container = null;
        if (Objects.equals("project", contextVo.getBizType())) {
            if (contextVo.getProjectVo() != null) {
                container = contextVo.getProjectVo().getConfig();
            }
        } else {
            AppVo appVo = contextVo.getAppVo();
            if (appVo != null) {
                container = appVo.getConfig();
            }
        }
        InvokeNotifyPolicyConfigVo configVo = null;
        if (container != null) {
            configVo = container.getObject(NOTIFY_POLICY_CONFIG, InvokeNotifyPolicyConfigVo.class);
        }
        if (configVo == null) {
            configVo = new InvokeNotifyPolicyConfigVo();
        }
        configVo.setHandler(handler.getClassName());
        return configVo;
    }

    private void enrichContext(RdmNotifyContextVo contextVo) {
        ProjectVo projectVo = contextVo.getProjectVo();
        if (projectVo == null) {
            Long projectId = getProjectId(contextVo);
            if (projectId != null) {
                projectVo = projectMapper.getProjectById(projectId);
                contextVo.setProjectVo(projectVo);
            }
        }
        if (!Objects.equals("project", contextVo.getBizType()) && contextVo.getAppVo() == null) {
            AppVo appVo = null;
            if (Objects.equals("iteration", contextVo.getBizType()) && projectVo != null) {
                appVo = appMapper.getAppByProjectIdAndType(projectVo.getId(), AppType.ITERATION.getValue());
            } else if (contextVo.getIssueVo() != null && contextVo.getIssueVo().getAppId() != null) {
                appVo = appMapper.getAppById(contextVo.getIssueVo().getAppId());
            }
            contextVo.setAppVo(appVo);
        }
    }

    private Long getProjectId(RdmNotifyContextVo contextVo) {
        if (contextVo.getIterationVo() != null) {
            return contextVo.getIterationVo().getProjectId();
        }
        if (contextVo.getIssueVo() != null) {
            return contextVo.getIssueVo().getProjectId();
        }
        return null;
    }

    private Map<String, List<NotifyReceiverVo>> buildReceiverMap(RdmNotifyContextVo contextVo) {
        Map<String, List<NotifyReceiverVo>> receiverMap = new HashMap<>();
        ProjectVo projectVo = contextVo.getProjectVo();
        if (projectVo != null && CollectionUtils.isNotEmpty(projectVo.getUserList())) {
            for (ProjectUserVo userVo : projectVo.getUserList()) {
                if (CollectionUtils.isEmpty(userVo.getUserTypeList())) {
                    continue;
                }
                for (ProjectUserTypeVo userTypeVo : userVo.getUserTypeList()) {
                    addProjectUser(receiverMap, userTypeVo.getUserType(), userVo.getUserId());
                }
            }
        }
        IssueVo issueVo = contextVo.getIssueVo();
        if (issueVo != null) {
            addUser(receiverMap, IssueUserType.OWNER.getValue(), issueVo.getCreateUser());
            if (CollectionUtils.isNotEmpty(issueVo.getUserIdList())) {
                for (String userUuid : issueVo.getUserIdList()) {
                    addUser(receiverMap, IssueUserType.WORKER.getValue(), userUuid);
                }
            }
        }
        addUser(receiverMap, IssueUserType.OPERATOR.getValue(), contextVo.getOperatorUuid());
        if (contextVo.getCommentVo() != null) {
            addUser(receiverMap, IssueUserType.COMMENTER.getValue(), contextVo.getCommentVo().getFcu());
        }
        addUser(receiverMap, IssueUserType.REPLY_USER.getValue(), contextVo.getReplyUserUuid());
        return receiverMap;
    }

    private void addProjectUser(Map<String, List<NotifyReceiverVo>> receiverMap, String userType, String userUuid) {
        if (Objects.equals(userType, ProjectUserType.OWNER.getValue())) {
            addUser(receiverMap, IssueUserType.PROJECT_OWNER.getValue(), userUuid);
        } else if (Objects.equals(userType, ProjectUserType.LEADER.getValue())) {
            addUser(receiverMap, IssueUserType.PROJECT_LEADER.getValue(), userUuid);
        } else if (Objects.equals(userType, ProjectUserType.MEMBER.getValue())) {
            addUser(receiverMap, IssueUserType.PROJECT_MEMBER.getValue(), userUuid);
        }
    }

    private void addUser(Map<String, List<NotifyReceiverVo>> receiverMap, String receiverKey, String userUuid) {
        if (StringUtils.isBlank(userUuid)) {
            return;
        }
        String normalizedUuid = userUuid.replace(GroupSearch.USER.getValuePlugin(), "");
        List<NotifyReceiverVo> receiverList = receiverMap.computeIfAbsent(receiverKey, key -> new ArrayList<>());
        Set<String> uuidSet = new HashSet<>();
        for (NotifyReceiverVo receiverVo : receiverList) {
            uuidSet.add(receiverVo.getUuid());
        }
        if (uuidSet.add(normalizedUuid)) {
            receiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), normalizedUuid));
        }
    }

    private void fillOperator(RdmNotifyContextVo contextVo) {
        UserContext userContext = UserContext.get();
        if (userContext == null) {
            return;
        }
        if (StringUtils.isBlank(contextVo.getOperatorUuid())) {
            contextVo.setOperatorUuid(userContext.getUserUuid(true));
        }
        if (StringUtils.isBlank(contextVo.getOperatorName())) {
            contextVo.setOperatorName(userContext.getUserName());
        }
    }
}
