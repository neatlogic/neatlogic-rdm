package codedriver.module.rdm.constants;

/**
 * @ClassName FieldType
 * @Description 字段类型枚举类
 * @Auther
 * @Date 2019/12/4 15:16
 **/
public enum FieldType {
    TEXT("text", "单行文本", "单行文本输入，输入长度不能超过200字符。"),
    LONGTEXT("longtext", "多行文本", "多行文本输入，输入长度不限制，但输入值不可用于搜索。"),
    DECIMAL("decimal", "小数", "小数输入，可加入单位。"),
    INTEGER("integer", "整数", "整数输入，可加入单位。"),
    CALENDAR("calendar", "日历", "日历输入，可调整格式"),
    SELECT("select", "单选下拉", "下拉菜单列表，只能选择一项"),
    MSELECT("mselect", "多选下拉", "下拉菜单列表，可以选择多项"),
    RADIO("radio", "单选", "单选框，只能选择一项"),
    CHECKBOX("checkbox", "多选", "多选框，可以选择多项"),
    INPUTSELECT("inputselect", "可搜索下拉", "可搜索下拉菜单列表，根据关键字到指定接口获取选项，支持单选或多选"),
    FILE("file", "附件", "上传附件"),
    USER("user", "单个用户", "单个用户选择组件"),
    MUSER("muser", "多个用户", "多个用户选择组件"),
    TEAM("team", "单个组织", "单个组织选择组件"),
    MTEAM("mteam", "多个组织", "多个组织选择组件");


    private String type;
    private String name;
    private String description;

    FieldType(String _type, String _name, String _description) {
        this.type = _type;
        this.name = _name;
        this.description = _description;
    }

    public static String getNameByType(String _type) {
        for (FieldType field : FieldType.values()) {
            if (field.getType().equals(_type)) {
                return field.getName();
            }
        }
        return "未定义的类型";
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
