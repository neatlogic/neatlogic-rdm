package codedriver.module.rdm.constants;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 10:37
 **/
public enum  ModuleType {
    PROCESS("process", "过程域"),ACKNOWLEDGE("acknowledge", "知识库");

    private String value;
    private String text;

    private ModuleType(String _value, String _text){
        value = _value;
        text = _text;
    }

    public String getValue(){
        return value;
    }

    public String getText(){
        return text;
    }

    public static String getText(String value){
        for (ModuleType f : ModuleType.values()){
            if (f.getValue().equals(value)){
                return f.getText();
            }
        }
        return "";
    }
}
