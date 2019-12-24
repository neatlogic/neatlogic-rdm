package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.*;

import java.util.List;

/**
 * @ClassName ProjectService
 * @Description
 * @Auther
 * @Date 2019/12/9 11:40
 **/
public interface ProjectService {
    List <ProjectVo> searchProject(ProjectVo projectVo);

    String saveProject(ProjectVo projectVo);

    List<ProjectProcessAreaVo> searchProjectProcessArea(ProjectProcessAreaVo processAreaVo);

    void saveProjectProcessArea(ProjectProcessAreaVo processAreaVo);

    void saveProjectProcessAreaTemplate(ProjectProcessAreaTemplateVo templateVo);

    void saveProjectProcessField(ProjectProcessAreaFieldVo fieldVo);
}
