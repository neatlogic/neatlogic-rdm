package codedriver.module.rdm.constants;

/**
 * @ClassName ProcessAreaType
 * @Description 状态类型
 * @Auther
 * @Date 2019/12/4 19:30
 **/
public enum ProjectStatusType {

    START(1, "开始状态"),
    RUN(2, "运行状态"),
    STOP(3, "结束状态");


    private Integer id;
    private String name;

    ProjectStatusType(Integer _id, String _name) {
        this.id = _id;
        this.name = _name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
