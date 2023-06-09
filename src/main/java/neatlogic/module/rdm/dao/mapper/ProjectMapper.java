/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.module.rdm.dao.mapper;

import neatlogic.framework.rdm.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper {
    List<AppStatusRelVo> getAppStatusRelByProjectId(Long projectId);

    List<AppAttrVo> getAppAttrByProjectId(Long projectId);

    List<AppStatusVo> getAppStatusByProjectId(Long projectId);

    List<AppVo> getAppByProjectId(Long projectId);

    List<ProjectUserVo> getProjectUserList(ProjectUserVo projectUserVo);

    List<String> getProjectAppTypeByProjectId(Long projectId);

    ProjectVo getProjectByAppId(Long appId);

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
