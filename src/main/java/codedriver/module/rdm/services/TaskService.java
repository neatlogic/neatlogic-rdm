package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.TaskAssociateVo;
import codedriver.module.rdm.dto.TaskVo;

import java.util.List;

/**
 * @ClassName TaskService
 * @Description
 * @Auther
 * @Date 2019/12/24 10:42
 **/
public interface TaskService {
    String saveTask(TaskVo taskVo);

    void associateTask(List<TaskAssociateVo> associateList);

    void deleteAssociate(String taskUuid, String targetUuid);
}
