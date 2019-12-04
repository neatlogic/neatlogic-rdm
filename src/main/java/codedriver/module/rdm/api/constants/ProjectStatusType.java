package codedriver.module.rdm.api.constants;

/**
 * @ClassName ProcessAreaType
 * @Description 状态类型
 * @Auther fandong
 * @Date 2019/12/4 19:30
 **/
public enum ProjectStatusType {

    START(1, "开始态"),
    RUN(2, "运行态"),
    STOP(3, "结束态");


    private Integer id;
    private String name;

    private ProjectStatusType(Integer _id, String _name) {
        this.id = _id;
        this.name = _name;
    }
}
