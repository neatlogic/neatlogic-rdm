package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ProcessAreaVo;
import codedriver.module.rdm.dto.ProjectVo;

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
}
