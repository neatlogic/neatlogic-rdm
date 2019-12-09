package codedriver.module.rdm.dto;

/**
 * @ClassName FilterVo
 * @Description 任务查询过滤类(过滤器)
 * @Auther
 * @Date 2019/12/4 17:38
 **/
public class FilterVo {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 过滤器uuid
     */
    private String uuid;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 项目uuid
     */
    private Long processAreaId;

    /**
     * 过滤器名称
     */
    private String name;

    /**
     * 过滤器配置(条件)
     */
    private String config;

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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
