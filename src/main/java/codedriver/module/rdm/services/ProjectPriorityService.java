package codedriver.module.rdm.services;


import codedriver.module.rdm.dto.ProjectPriorityVo;

import java.util.List;

/**
 * @ClassName ProjectPriorityService
 * @Description
 * @Auther
 * @Date 2019/12/13 14:32
 **/
public interface ProjectPriorityService {

    String saveProjectPriority(ProjectPriorityVo projectPriorityVo);

    List<ProjectPriorityVo> searchProjectPriority(ProjectPriorityVo projectPriorityVo);
}
