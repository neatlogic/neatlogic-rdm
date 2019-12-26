package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.TaskFieldVo;
import codedriver.module.rdm.dto.TaskFileVo;
import codedriver.module.rdm.dto.TaskVo;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName TaskMapper
 * @Description
 * @Auther fandong
 * @Date 2019/12/24 11:22
 **/
public interface TaskMapper {
    void insertTask(TaskVo taskVo);

    void insertTaskProcessAccount(@Param("taskUuid") String taskUuid, @Param("userId")String userId);

    void insertTaskField(TaskFieldVo taskField);

    void insertTaskDescription(@Param("taskUuid") String taskUuid, @Param("description")String description);

    void insertTaskFile(TaskFileVo taskFileVo);

    void deleteTaskFile(TaskFileVo taskFileVo);
}
