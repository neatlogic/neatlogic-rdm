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
