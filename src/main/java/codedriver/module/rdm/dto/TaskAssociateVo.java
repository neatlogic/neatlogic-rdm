package codedriver.module.rdm.dto;

/**
 * @ClassName TaskAssociateVo
 * @Description
 * @Auther
 * @Date 2019/12/25 14:41
 **/
public class TaskAssociateVo {
    private String taskUuid;
    private String targetTaskUuid;
    private String type;

    public TaskAssociateVo(String _taskUuid, String _targetTaskUuid, String _type) {
        this.taskUuid = _taskUuid;
        this.targetTaskUuid = _targetTaskUuid;
        this.type = _type;
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
    }

    public String getTargetTaskUuid() {
        return targetTaskUuid;
    }

    public void setTargetTaskUuid(String targetTaskUuid) {
        this.targetTaskUuid = targetTaskUuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
