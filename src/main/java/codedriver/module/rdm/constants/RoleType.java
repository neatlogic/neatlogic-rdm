package codedriver.module.rdm.constants;

public enum RoleType {
    ADMIN("系统管理员","admin"),
    MANAGER("项目管理员", "manager"),
    ORDINARY("普通成员", "ordinary");

    private String text;
    private String value;

    private RoleType(String _text, String _value){
        this.text = _text;
        this.value = _value;
    }

    public String getValue(){
        return value;
    }

    public String getText(){
        return text;
    }

    public static String getText(String value){
        for (RoleType f : RoleType.values()){
            if (f.getValue().equals(value)){
                return f.getText();
            }
        }
        return "";
    }
}
