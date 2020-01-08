package codedriver.module.rdm.constants;

public enum RoleType {
    ADMIN("admin","系统管理员"),
    MANAGER("manager", "项目管理员"),
    ORDINARY("ordinary", "普通成员");

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
