package codedriver.module.rdm.dto;

/**
 * @ClassName CategoryVo
 * @Description 分类
 * @Auther
 * @Date 2019/12/4 18:36
 **/
public class CategoryVo {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 类型uuid
     */
    private String uuid;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型描述
     */
    private String description;

    /**
     * 父类型id
     */
    private Long parentId;

    /**
     * 父类型uuid
     */
    private String parentUuid;

    /**
     * 过程域id
     */
    private Long processAreaId;

    /**
     * 过程域uuid
     */
    private String processAreaUuid;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 项目uuid
     */
    private String projectUuid;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProcessAreaId() {
        return processAreaId;
    }

    public void setProcessAreaId(Long processAreaId) {
        this.processAreaId = processAreaId;
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }
}
