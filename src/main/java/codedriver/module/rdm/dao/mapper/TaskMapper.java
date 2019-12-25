package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.TaskAssociateVo;
import codedriver.module.rdm.dto.TaskCommentVo;
import codedriver.module.rdm.dto.TaskFieldVo;
import codedriver.module.rdm.dto.TaskVo;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName TaskMapper
 * @Description
 * @Auther
 * @Date 2019/12/24 11:22
 **/
public interface TaskMapper {
    void insertTask(TaskVo taskVo);

    void insertTaskProcessAccount(@Param("taskUuid") String taskUuid, @Param("userId")String userId);

    void insertTaskField(TaskFieldVo taskField);

    void insertTaskDescription(@Param("taskUuid") String taskUuid, @Param("description")String description);

    void replaceAssociate(TaskAssociateVo taskAssociateVo);

    void deleteAssociate(@Param("taskUuid") String taskUuid, @Param("targetTaskUuid") String targetTaskUuid);

    void insertTaskComment(TaskCommentVo taskCommentVo);

    void updateTaskComment(TaskCommentVo taskCommentVo);

    void deleteComment(Long id);
}
