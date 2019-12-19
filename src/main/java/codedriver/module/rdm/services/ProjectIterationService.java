package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ProjectIterationVo;
import codedriver.module.rdm.dto.ProjectPriorityVo;

import java.util.List;

/**
 * @ClassName ProjectIterationService
 * @Description
 * @Auther 
 * @Date 2019/12/18 15:52
 **/
public interface ProjectIterationService {
    String saveProjectIteration(ProjectIterationVo projectIterationVo);

    List<ProjectPriorityVo> searchProjectIteration(ProjectIterationVo projectIterationVo);
}
