package codedriver.module.rdm.dto;

import java.util.List;

/**
 * @ClassName ProjectVo
 * @Description 项目类
 * @Auther r2d2
 * @Date 2019/12/4 15:55
 **/
public class ProjectVo {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目uuid
     */
    private String uuid;

    /**
     * 模板
     */
    private Long templateId;

    /**
     * 模板uuid
     */
    private String templateUuid;

    private List<FieldVo> fieldList;

    private List<IterationVo> iterationList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<FieldVo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldVo> fieldList) {
        this.fieldList = fieldList;
    }

    public List<IterationVo> getIterationList() {
        return iterationList;
    }

    public void setIterationList(List<IterationVo> iterationList) {
        this.iterationList = iterationList;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateUuid() {
        return templateUuid;
    }

    public void setTemplateUuid(String templateUuid) {
        this.templateUuid = templateUuid;
    }
}
