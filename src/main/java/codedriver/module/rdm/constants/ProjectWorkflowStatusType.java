package codedriver.module.rdm.constants;

/**
 * @ClassName ProcessAreaType
 * @Description 状态类型
 * @Auther
 * @Date 2019/12/4 19:30
 **/
public enum ProjectWorkflowStatusType {

    START("start", "开始状态"),
    RUN("run", "运行状态"),
    STOP("stop", "结束状态");


    private String name;
    private String description;

    ProjectWorkflowStatusType(String _name, String _description) {
        this.name = _name;
        this.description = _description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
