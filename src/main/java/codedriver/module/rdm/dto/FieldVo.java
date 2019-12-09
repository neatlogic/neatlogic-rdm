package codedriver.module.rdm.dto;

import codedriver.module.rdm.constants.FieldType;

/**
 * @ClassName FieldVo
 * @Description 属性类
 * @Auther r2d2
 * @Date 2019/12/4 14:38
 **/
public class FieldVo {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 属性uuid
     */
    private String uuid;

    /**
     * 过程域id
     */
    private Long processAreaId;

    /**
     * 属性英文名
     */
    private String field;

    /**
     * 属性中文名称
     */
    private String name;

    /**
     * 属性别名
     */
    private String alias;

    /**
     * 属性描述
     */
    private String description;

    /**
     * 属性类型(数字)
     */
    private Integer type;

    /**
     * 属性类型名称
     */
    private String typeName;

    /**
     * 属性配置
     */
    private String config;

    /**
     * 是否是系统属性(0否  1是)
     */
    private Integer isSystemField;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getProcessAreaId() {
        return processAreaId;
    }

    public void setProcessAreaId(Long processAreaId) {
        this.processAreaId = processAreaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return FieldType.getNameById(this.type);
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Integer getIsSystemField() {
        return isSystemField;
    }

    public void setIsSystemField(Integer isSystemField) {
        this.isSystemField = isSystemField;
    }
}
