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

package neatlogic.module.rdm.auth;

import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectAuthManager {
    private static ProjectMapper projectMapper;

    @Autowired
    public ProjectAuthManager(ProjectMapper _projectMapper) {
        projectMapper = _projectMapper;
    }

    private static boolean checkAuth(ProjectVo projectVo, ProjectUserType... userTypes) {
        for (ProjectUserType userType : userTypes) {
            switch (userType) {
                case LEADER:
                    if (projectVo.getIsLeader()) {
                        return true;
                    }
                case MEMBER:
                    if (projectVo.getIsMember()) {
                        return true;
                    }
                case OWNER:
                    if (projectVo.getIsOwner()) {
                        return true;
                    }
            }
        }
        return false;
    }

    public static boolean checkAppAuth(Long appId, ProjectUserType... userTypes) {
        ProjectVo projectVo = projectMapper.getProjectByAppId(appId);
        if (projectVo == null) {
            throw new ProjectNotFoundException();
        }
        if (userTypes != null) {
            return checkAuth(projectVo, userTypes);
        }
        return true;
    }

    public static boolean checkProjectAuth(Long projectId, ProjectUserType... userTypes) {
        ProjectVo projectVo = projectMapper.getProjectById(projectId);
        if (projectVo == null) {
            throw new ProjectNotFoundException();
        }
        if (userTypes != null) {
            return checkAuth(projectVo, userTypes);
        }
        return true;
    }

    public static boolean checkIssueAuth(Long issueId, ProjectUserType... userTypes) {
        ProjectVo projectVo = projectMapper.getProjectByIssueId(issueId);
        if (projectVo == null) {
            throw new ProjectNotFoundException();
        }
        if (userTypes != null) {
            return checkAuth(projectVo, userTypes);
        }
        return true;
    }
}
