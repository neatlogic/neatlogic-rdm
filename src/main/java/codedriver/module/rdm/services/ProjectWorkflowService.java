package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ProjectStatusVo;

import java.util.List;

/**
 * @ClassName ProjectStatusService
 * @Description
 * @Auther
 * @Date 2019/12/13 14:32
 **/
public interface ProjectWorkflowService {

    void saveProjectWorkFlow(String projectUuid, String processAreaUuid, List<ProjectStatusVo> statusList);

    String saveProjectStatus(ProjectStatusVo projectStatusVo);

}
