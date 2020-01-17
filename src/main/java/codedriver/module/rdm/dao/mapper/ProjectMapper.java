package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectProcessAreaFieldVo;
import codedriver.module.rdm.dto.ProjectProcessAreaTemplateVo;
import codedriver.module.rdm.dto.ProjectProcessAreaVo;
import codedriver.module.rdm.dto.ProjectVo;

import java.util.List;

/**
 * @ClassName ProjectMapper
 * @Description
 * @Auther
 * @Date 2019/12/10 17:11
 **/
public interface ProjectMapper {
    int searchProjectCount(ProjectVo projectVo);

    int insertProject(ProjectVo projectVo);

    int updateProject(ProjectVo projectVo);

    void updateProjectProcessArea(ProjectProcessAreaVo processAreaVo);

    void updteProjectProcessAreaTemplate(ProjectProcessAreaTemplateVo templateVo);

    void updateProjectProcessAreaFieldConfig(ProjectProcessAreaFieldVo fieldVo);

    void updateProjectProcessAreaFieldRequired(ProjectProcessAreaFieldVo fieldVo);

    List<ProjectVo> searchProject(ProjectVo projectVo);

    List<ProjectProcessAreaVo> getProjectProcessArea(ProjectProcessAreaVo areaVo);

    ProjectVo getProjectByUuid(String uuid);

    void insertProjectProcessArea(ProjectProcessAreaVo processAreaVo);

    void insertProjectProcessAreaField(ProjectProcessAreaFieldVo processAreaFieldVo);

    void insertProjectProcessAreaTemplate(ProjectProcessAreaTemplateVo templateVo);

    void deleteProjectProcessAreaCustomFiled(ProjectProcessAreaFieldVo fieldVo);
}
