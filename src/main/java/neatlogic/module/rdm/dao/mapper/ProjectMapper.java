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

    ProjectVo getProjectByName(String name);

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
