package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

import java.util.List;

/**
 * @ClassName ProjectVo
 * @Description 项目类
 * @Auther
 * @Date 2019/12/4 15:55
 **/
public class ProjectVo extends BasePageVo {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 项目查询关键字
     */
    private transient String keyword;

    /**
     * 项目名称
     */
    @EntityField(name = "项目名称", type = ApiParamType.STRING)
    private String name;

    /**
     * 项目uuid
     */
    @EntityField(name = "项目uuid", type = ApiParamType.STRING)
    private String uuid;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目状态
     */
    private String status;

    /**
     * 父项目Uuid
     */
    private String parentUuid;

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

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private String updateTime;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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


    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }
}
