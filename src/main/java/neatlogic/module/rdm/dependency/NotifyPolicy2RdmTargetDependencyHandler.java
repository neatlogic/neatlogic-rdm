/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.dependency;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.dependency.constvalue.FrameworkFromType;
import neatlogic.framework.dependency.core.DefaultDependencyHandlerBase;
import neatlogic.framework.dependency.core.IFromType;
import neatlogic.framework.dependency.dto.DependencyInfoVo;
import neatlogic.framework.dependency.dto.DependencyVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知策略到RDM项目或App_type覆盖配置的引用关系。
 */
@Component
public class NotifyPolicy2RdmTargetDependencyHandler extends DefaultDependencyHandlerBase {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AppMapper appMapper;

    @Override
    protected DependencyInfoVo parse(DependencyVo dependencyVo) {
        String[] target = dependencyVo.getTo().split("#", 2);
        if (target.length != 2) {
            return null;
        }
        Long targetId = Long.valueOf(target[1]);
        ProjectVo projectVo;
        String targetName;
        if ("project".equals(target[0])) {
            projectVo = projectMapper.getProjectById(targetId);
            if (projectVo == null) {
                return null;
            }
            targetName = projectVo.getName() + " / 项目通知";
        } else if ("app".equals(target[0])) {
            AppVo appVo = appMapper.getAppById(targetId);
            if (appVo == null) {
                return null;
            }
            projectVo = projectMapper.getProjectById(appVo.getProjectId());
            if (projectVo == null) {
                return null;
            }
            targetName = projectVo.getName() + " / " + appVo.getName();
        } else {
            return null;
        }
        JSONObject config = new JSONObject();
        config.put("projectId", projectVo.getId());
        config.put("targetType", target[0]);
        config.put("targetId", targetId);
        List<String> pathList = new ArrayList<>();
        pathList.add("研发管理");
        pathList.add(projectVo.getName());
        String url = "/" + TenantContext.get().getTenantUuid() + "/rdm.html#/project-edit/${DATA.projectId}?notifyTargetType=${DATA.targetType}&notifyTargetId=${DATA.targetId}";
        return new DependencyInfoVo(dependencyVo.getTo(), config, targetName, pathList, url, getGroupName());
    }

    @Override
    public IFromType getFromType() {
        return FrameworkFromType.NOTIFY_POLICY;
    }
}
