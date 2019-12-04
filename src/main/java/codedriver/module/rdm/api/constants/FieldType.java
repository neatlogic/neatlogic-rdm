package codedriver.module.rdm.api.constants;

/**
 * @ClassName FieldType
 * @Description 字段类型枚举类
 * @Auther fandong
 * @Date 2019/12/4 15:16
 **/
public enum FieldType {
    FILE(1, "文件"),
    TEXT(2, "文本域"),
    NUMBER(3, "数值"),
    DATE(4, "日期选择"),
    SELECT(5, "单选框"),
    STRING(6, "单行文本框"),
    MULTIPLESELECT(7, "多选框");


    private Integer id;
    private String name;

    private FieldType(Integer _id, String _name) {
        this.id = _id;
        this.name = _name;
    }

    public static String getNameById(Integer _id){
        for (FieldType field : FieldType.values()) {
            if (field.getId().equals(_id)) {
                return field.getName();
            }
        }
        return "未定义的类型";
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
