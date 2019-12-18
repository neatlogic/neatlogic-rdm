package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-17 16:10
 **/
public class TemplateProcessAreaFieldVo {
    private Long id;
    private String fieldUuid;
    private String templateUuid;
    private String processAreaUuid;

    @EntityField(name ="属性键名", type = ApiParamType.STRING)
    private String field;

    @EntityField(name = "属性显示名", type = ApiParamType.STRING)
    private String fieldName;

    @EntityField(name = "属性控件类型", type = ApiParamType.STRING)
    private String fieldType;

    @EntityField(name = "属性控件配置", type = ApiParamType.STRING)
    private String config;

    @EntityField(name = "是否为系统属性", type = ApiParamType.INTEGER)
    private int isSystem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateUuid() {
        return templateUuid;
    }

    public void setTemplateUuid(String templateUuid) {
        this.templateUuid = templateUuid;
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldUuid() {
        return fieldUuid;
    }

    public void setFieldUuid(String fieldUuid) {
        this.fieldUuid = fieldUuid;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public int getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(int isSystem) {
        this.isSystem = isSystem;
    }
}