package codedriver.module.rdm.constants;

public enum ActionType {
    MERGER("merge", "合并"),
    DELETE("delete", "删除"),
    IMPORT("import", "导入"),
    CREATE("create", "创建"),
    EDIT("edit", "编辑"),
    EXPORT("export", "导出"),
    UPLOAD("upload", "附件上传/关联"),
    DOWNLOAD("download", "附件下载/预览"),
    REMOVE("remove", "附件删除/解除关联"),
    CIRCULATION("circulation","流转"),
    MOVE("move","移动");

    private String value;
    private String text;

    private ActionType(String _value, String _text){
        this.value = _value;
        this.text=_text;
    }

    public String getValue(){
        return value;
    }

    public String getText(){
        return text;
    }

    public static String getText(String value){
        for (ActionType f : ActionType.values()){
            if (f.getValue().equals(value)){
                return f.getText();
            }
        }
        return "";
    }
}
