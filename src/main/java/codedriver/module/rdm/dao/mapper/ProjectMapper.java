/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.dao.mapper;

import codedriver.framework.rdm.dto.*;

import java.util.List;

public interface ProjectMapper {
    int getMaxObjectAttrSortByObjectId(Long objectId);

    int checkAttrNameIsExists(ObjectAttrVo objectAttrVo);

    ObjectAttrVo getAttrById(Long attrId);

    List<ObjectAttrVo> searchObjectAttr(ObjectAttrVo objectAttrVo);

    ObjectVo getObjectById(Long id);

    List<ObjectVo> getObjectDetailByProjectId(Long projectId);

    ProjectVo getProjectById(Long id);

    int searchProjectCount(ProjectVo projectVo);

    List<ProjectVo> searchProject(ProjectVo projectVo);

    ProjectTemplateVo getProjectTemplateById(Long templateId);

    List<ProjectStatusVo> getStatusByProjectId(Long projectId);

    List<ProjectStatusRelVo> getStatusRelByProjectId(Long projectId);

    List<ObjectStatusVo> getStatusByObjectId(Long objectId);

    List<ObjectStatusRelVo> getStatusRelByObjectId(Long objectId);

    ProjectStatusVo getStatusById(Long id);

    void insertObject(ObjectVo objectVo);


    void insertProjectUser(ProjectUserVo projectUserVo);

    void updateObjectAttrSort(ObjectAttrVo objectAttrVo);

    void insertProjectStatusRel(ProjectStatusRelVo projectStatusRelVo);

    void updateObjectAttrIsActive(ObjectAttrVo objectAttrVo);

    void updateProject(ProjectVo projectVo);

    void updateProjectStatus(ProjectStatusVo projectStatusVo);

    void updateObjectAttr(ObjectAttrVo objectAttrVo);

    void insertObjectAttr(ObjectAttrVo objectAttrVo);

    void insertProject(ProjectVo projectVo);

    void insertProjectStatus(ProjectStatusVo projectStatusVo);

    int checkProjectNameIsExists(ProjectVo projectVo);

    void deleteObjectAttrById(Long id);

    void deleteProjectUserByProjectId(Long projectId);

    void deleteProjectById(Long id);

    void deleteProjectStatusRel(ProjectStatusRelVo projectStatusRelVo);

    void deleteObjectById(Long id);

}
