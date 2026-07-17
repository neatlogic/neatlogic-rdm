/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.service;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthActionChecker;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.dependency.core.DependencyManager;
import neatlogic.framework.notify.crossover.INotifyServiceCrossoverService;
import neatlogic.framework.notify.dao.mapper.NotifyMapper;
import neatlogic.framework.notify.dto.InvokeNotifyPolicyConfigVo;
import neatlogic.framework.notify.dto.NotifyPolicyVo;
import neatlogic.framework.notify.dto.NotifyTriggerVo;
import neatlogic.framework.notify.dto.ParamMappingVo;
import neatlogic.framework.notify.exception.NotifyPolicyNotFoundException;
import neatlogic.framework.rdm.auth.label.PROJECT_MANAGE;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.enums.AppType;
import neatlogic.framework.rdm.exception.ProjectNotAuthException;
import neatlogic.framework.rdm.exception.ProjectObjectNotFoundException;
import neatlogic.framework.rdm.exception.RdmNotifyPolicyConfigInvalidException;
import neatlogic.framework.rdm.notify.core.IRdmNotifyPolicyHandler;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.notify.core.RdmNotifyPolicyHandlerFactory;
import neatlogic.module.rdm.dependency.NotifyPolicy2RdmTargetDependencyHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 统一维护项目和App_type级通知策略，handler只由服务端按目标类型生成。
 */
@Service
public class RdmNotifyConfigServiceImpl implements RdmNotifyConfigService {

    private static final String NOTIFY_POLICY_CONFIG = "notifyPolicyConfig";

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private AttrMapper attrMapper;

    @Resource
    private NotifyMapper notifyMapper;

    @Override
    public JSONObject getConfig(String targetType, Long targetId) {
        TargetContext target = resolveTarget(targetType, targetId);
        checkAuth(target.getProjectVo());
        InvokeNotifyPolicyConfigVo configVo = readConfig(target);
        INotifyServiceCrossoverService notifyService = CrossoverServiceFactory.getApi(INotifyServiceCrossoverService.class);
        configVo = notifyService.regulateNotifyPolicyConfig(configVo);
        JSONObject result = (JSONObject) JSONObject.toJSON(configVo);
        result.put("handler", target.getHandler().getClassName());
        result.put("bizType", target.getHandler().getBizType());
        NotifyPolicyVo defaultPolicyVo = notifyMapper.getDefaultNotifyPolicyByHandler(target.getHandler().getClassName());
        if (defaultPolicyVo != null) {
            result.put("defaultPolicyId", defaultPolicyVo.getId());
            result.put("defaultPolicyName", defaultPolicyVo.getName());
        }
        return result;
    }

    @Override
    @Transactional
    public void saveConfig(String targetType, Long targetId, InvokeNotifyPolicyConfigVo configVo) {
        TargetContext target = resolveTarget(targetType, targetId);
        checkAuth(target.getProjectVo());
        if (configVo == null) {
            throw new RdmNotifyPolicyConfigInvalidException("通知策略配置不能为空");
        }
        configVo.setHandler(target.getHandler().getClassName());
        validateConfig(configVo, target);
        INotifyServiceCrossoverService notifyService = CrossoverServiceFactory.getApi(INotifyServiceCrossoverService.class);
        InvokeNotifyPolicyConfigVo normalizedConfig = notifyService.regulateNotifyPolicyConfig(configVo);
        JSONObject container = target.getContainer();
        container.put(NOTIFY_POLICY_CONFIG, normalizedConfig);
        String dependencyTarget = target.getDependencyTarget();
        DependencyManager.delete(NotifyPolicy2RdmTargetDependencyHandler.class, dependencyTarget);
        if (normalizedConfig.getIsCustom() == 1 && normalizedConfig.getPolicyId() != null) {
            DependencyManager.insert(NotifyPolicy2RdmTargetDependencyHandler.class, normalizedConfig.getPolicyId(), dependencyTarget);
        }
        if (target.getProjectTarget() != null) {
            ProjectVo projectVo = target.getProjectTarget();
            projectVo.setConfig(container);
            projectVo.setLcu(UserContext.get().getUserUuid(true));
            projectMapper.updateProjectConfig(projectVo);
        } else {
            AppVo appVo = target.getAppTarget();
            appVo.setConfig(container);
            appMapper.saveAppConfig(appVo);
        }
    }

    private void validateConfig(InvokeNotifyPolicyConfigVo configVo, TargetContext target) {
        if (configVo == null) {
            throw new RdmNotifyPolicyConfigInvalidException("通知策略配置不能为空");
        }
        IRdmNotifyPolicyHandler handler = target.getHandler();
        if (configVo.getIsCustom() == 1) {
            if (configVo.getPolicyId() == null) {
                throw new RdmNotifyPolicyConfigInvalidException("自定义通知策略不能为空");
            }
            NotifyPolicyVo policyVo = notifyMapper.getNotifyPolicyById(configVo.getPolicyId());
            if (policyVo == null) {
                throw new NotifyPolicyNotFoundException(configVo.getPolicyId());
            }
            if (!Objects.equals(policyVo.getHandler(), handler.getClassName())) {
                throw new RdmNotifyPolicyConfigInvalidException("通知策略与当前业务分类不匹配");
            }
        }
        Set<String> triggerSet = new HashSet<>();
        for (NotifyTriggerVo triggerVo : handler.getNotifyTriggerList()) {
            triggerSet.add(triggerVo.getTrigger());
        }
        if (CollectionUtils.isNotEmpty(configVo.getExcludeTriggerList())) {
            for (String trigger : configVo.getExcludeTriggerList()) {
                if (!triggerSet.contains(trigger)) {
                    throw new RdmNotifyPolicyConfigInvalidException("通知触发点“" + trigger + "”不属于当前业务分类");
                }
            }
        }
        validateParamMapping(configVo, target);
    }

    /**
     * App级模板参数只能映射到当前App已启用的自定义属性，禁止客户端提交固定文本或其他App的属性。
     */
    private void validateParamMapping(InvokeNotifyPolicyConfigVo configVo, TargetContext target) {
        if (target.getAppTarget() == null || CollectionUtils.isEmpty(configVo.getParamMappingList())) {
            return;
        }
        Set<String> customAttrUuidSet = new HashSet<>();
        List<AppAttrVo> attrList = attrMapper.getAttrByAppId(target.getAppTarget().getId());
        if (CollectionUtils.isNotEmpty(attrList)) {
            for (AppAttrVo appAttrVo : attrList) {
                if (Integer.valueOf(0).equals(appAttrVo.getIsPrivate()) && Integer.valueOf(1).equals(appAttrVo.getIsActive())) {
                    customAttrUuidSet.add(appAttrVo.getUuid());
                }
            }
        }
        for (ParamMappingVo paramMappingVo : configVo.getParamMappingList()) {
            if (!Objects.equals("attr", paramMappingVo.getType()) || !customAttrUuidSet.contains(paramMappingVo.getValue())) {
                throw new RdmNotifyPolicyConfigInvalidException("模板参数只能映射到当前App已启用的自定义属性");
            }
        }
    }

    private InvokeNotifyPolicyConfigVo readConfig(TargetContext target) {
        JSONObject config = target.getContainer();
        InvokeNotifyPolicyConfigVo configVo = config.getObject(NOTIFY_POLICY_CONFIG, InvokeNotifyPolicyConfigVo.class);
        if (configVo == null) {
            configVo = new InvokeNotifyPolicyConfigVo();
        }
        configVo.setHandler(target.getHandler().getClassName());
        return configVo;
    }

    private TargetContext resolveTarget(String targetType, Long targetId) {
        if (Objects.equals("project", targetType)) {
            ProjectVo projectVo = projectMapper.getProjectById(targetId);
            if (projectVo == null) {
                throw new ProjectObjectNotFoundException(targetId);
            }
            IRdmNotifyPolicyHandler handler = requireHandler("project");
            return new TargetContext(projectVo, projectVo, null, handler, projectVo.getConfig(), "project#" + targetId);
        }
        if (Objects.equals("app", targetType)) {
            AppVo appVo = appMapper.getAppById(targetId);
            if (appVo == null) {
                throw new ProjectObjectNotFoundException(targetId);
            }
            ProjectVo projectVo = projectMapper.getProjectById(appVo.getProjectId());
            if (projectVo == null) {
                throw new ProjectObjectNotFoundException(appVo.getProjectId());
            }
            String bizType = appVo.getType();
            if (Objects.equals(AppType.ITERATION.getValue(), appVo.getType())) {
                bizType = "iteration";
            }
            IRdmNotifyPolicyHandler handler = requireHandler(bizType);
            return new TargetContext(projectVo, null, appVo, handler, appVo.getConfig(), "app#" + targetId);
        }
        throw new RdmNotifyPolicyConfigInvalidException("通知策略目标类型仅支持project或app");
    }

    private IRdmNotifyPolicyHandler requireHandler(String bizType) {
        IRdmNotifyPolicyHandler handler = RdmNotifyPolicyHandlerFactory.getHandler(bizType);
        if (handler == null) {
            throw new RdmNotifyPolicyConfigInvalidException("业务分类“" + bizType + "”未启用通知处理器");
        }
        return handler;
    }

    private void checkAuth(ProjectVo projectVo) {
        if (AuthActionChecker.check(PROJECT_MANAGE.class)) {
            return;
        }
        if (!ProjectAuthManager.checkProjectAuth(projectVo.getId(), neatlogic.framework.rdm.enums.ProjectUserType.OWNER, neatlogic.framework.rdm.enums.ProjectUserType.LEADER)) {
            throw new ProjectNotAuthException(projectVo.getName());
        }
    }

    private static class TargetContext {
        private final ProjectVo projectVo;
        private final ProjectVo projectTarget;
        private final AppVo appTarget;
        private final IRdmNotifyPolicyHandler handler;
        private final JSONObject container;
        private final String dependencyTarget;

        private TargetContext(ProjectVo projectVo, ProjectVo projectTarget, AppVo appTarget, IRdmNotifyPolicyHandler handler, JSONObject container, String dependencyTarget) {
            this.projectVo = projectVo;
            this.projectTarget = projectTarget;
            this.appTarget = appTarget;
            this.handler = handler;
            this.dependencyTarget = dependencyTarget;
            if (container == null) {
                this.container = new JSONObject();
            } else {
                this.container = container;
            }
        }

        private ProjectVo getProjectVo() { return projectVo; }
        private ProjectVo getProjectTarget() { return projectTarget; }
        private AppVo getAppTarget() { return appTarget; }
        private IRdmNotifyPolicyHandler getHandler() { return handler; }
        private JSONObject getContainer() { return container; }
        private String getDependencyTarget() { return dependencyTarget; }
    }
}
