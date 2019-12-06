package codedriver.module.rdm.dto;

/**
 * @ClassName ProjectFieldVo
 * @Description 项目属性类
 * @Auther
 * @Date 2019/12/4 15:58
 **/
public class ProjectFieldVo extends ProjectVo {
    private String field;
    private String fieldUuid;
    private String fieldName;
    private String fieldConfig;
    private String fieldData;
    private Integer fieldType;
    private Long processAreaId;
    private Integer isSystemField;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldUuid() {
        return fieldUuid;
    }

    public void setFieldUuid(String fieldUuid) {
        this.fieldUuid = fieldUuid;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldConfig() {
        return fieldConfig;
    }

    public void setFieldConfig(String fieldConfig) {
        this.fieldConfig = fieldConfig;
    }

    public String getFieldData() {
        return fieldData;
    }

    public void setFieldData(String fieldData) {
        this.fieldData = fieldData;
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }

    public Long getProcessAreaId() {
        return processAreaId;
    }

    public void setProcessAreaId(Long processAreaId) {
        this.processAreaId = processAreaId;
    }

    public Integer getIsSystemField() {
        return isSystemField;
    }

    public void setIsSystemField(Integer isSystemField) {
        this.isSystemField = isSystemField;
    }
}
