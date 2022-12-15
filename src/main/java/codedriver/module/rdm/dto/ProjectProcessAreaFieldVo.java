/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-19 14:38
 **/
public class ProjectProcessAreaFieldVo {
    @EntityField(name = "主键ID", type = ApiParamType.LONG)
    private Long id;

    @EntityField(name = "项目uuid", type = ApiParamType.STRING)
    private String projectUuid;

    @EntityField(name = "过程域uuid", type = ApiParamType.STRING)
    private String processAreaUuid;

    @EntityField(name = "属性展示名称", type = ApiParamType.STRING)
    private String fieldName;

    @EntityField(name = "属性值名称", type = ApiParamType.STRING)
    private String field;

    @EntityField(name = "属性uuid", type = ApiParamType.STRING)
    private String fieldUuid;

    @EntityField(name = "控件类型", type = ApiParamType.STRING)
    private String fieldType;

    @EntityField(name = "属性配置", type = ApiParamType.JSONOBJECT)
    private String config;

    @EntityField(name = "是否为系统属性", type = ApiParamType.STRING)
    private int isSystem;

    @EntityField(name = "是否展示", type = ApiParamType.INTEGER)
    private int isShow;

    @EntityField(name = "是否必填", type = ApiParamType.INTEGER)
    private int isRequired;

    public int getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(int isRequired) {
        this.isRequired = isRequired;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

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

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}
