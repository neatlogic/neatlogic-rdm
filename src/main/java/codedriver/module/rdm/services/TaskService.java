package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.TaskFieldVo;
import codedriver.module.rdm.dto.TaskFileVo;
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

    List<Long> saveTaskFile(TaskVo taskVo);
}
