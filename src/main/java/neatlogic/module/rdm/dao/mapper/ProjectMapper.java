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

package neatlogic.module.rdm.dao.mapper;

import neatlogic.framework.rdm.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper {
    List<ProjectUserVo> searchProjectUser(ProjectUserVo projectUserVo);

    List<AppStatusRelVo> getAppStatusRelByProjectId(Long projectId);

    List<AppAttrVo> getAppAttrByProjectId(Long projectId);

    List<AppStatusVo> getAppStatusByProjectId(Long projectId);

    List<AppVo> getAppByProjectId(Long projectId);

    List<ProjectUserVo> getProjectUserList(ProjectUserVo projectUserVo);

    List<String> getProjectAppTypeByProjectId(Long projectId);

    ProjectVo getProjectByAppId(Long appId);

    ProjectVo getProjectByIssueId(Long issueId);

    ProjectVo getProjectById(Long id);

    int searchProjectCount(ProjectConditionVo projectVo);

    List<ProjectVo> searchProject(ProjectConditionVo projectVo);

    List<Long> searchProjectId(ProjectConditionVo projectVo);

    List<ProjectStatusVo> getStatusByProjectId(Long projectId);

    List<ProjectStatusRelVo> getStatusRelByProjectId(Long projectId);


    ProjectStatusVo getStatusById(Long id);


    void insertProjectUser(ProjectUserVo projectUserVo);


    void insertProjectStatusRel(ProjectStatusRelVo projectStatusRelVo);


    void updateProject(ProjectVo projectVo);

    void updateProjectStatus(ProjectStatusVo projectStatusVo);


    void insertProject(ProjectVo projectVo);

    void insertProjectStatus(ProjectStatusVo projectStatusVo);

    int checkProjectNameIsExists(ProjectVo projectVo);


    void deleteProjectUserByProjectId(@Param("projectId") Long projectId, @Param("userTypeList") List<String> userTypeList);

    void deleteProjectById(Long id);

    void deleteProjectStatusRel(ProjectStatusRelVo projectStatusRelVo);


}
