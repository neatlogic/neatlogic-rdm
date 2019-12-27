package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ProjectIterationVo;

import java.util.List;

/**
 * @ClassName ProjectIterationService
 * @Description
 * @Auther
 * @Date 2019/12/18 15:52
 **/
public interface ProjectIterationService {
    String saveProjectIteration(ProjectIterationVo projectIterationVo);

    List<ProjectIterationVo> searchProjectIteration(ProjectIterationVo projectIterationVo);

    void associateTask(String projectUuid, String projectIterationUuid, String action, List<String> taskList);
}
