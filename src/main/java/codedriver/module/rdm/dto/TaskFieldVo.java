package codedriver.module.rdm.dto;

/**
 * @ClassName TaskFieldVo
 * @Description Task属性类
 * @Auther
 * @Date 2019/12/4 15:58
 **/
public class TaskFieldVo extends FieldVo {

    private String taskUuid;

    public TaskFieldVo(FieldVo field) {
        super(field);
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
    }
}
