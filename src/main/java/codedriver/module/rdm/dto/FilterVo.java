package codedriver.module.rdm.dto;

/**
 * @ClassName FilterVo
 * @Description 任务查询过滤类
 * @Auther
 * @Date 2019/12/4 17:38
 **/
public class FilterVo {
    private Long id;
    private String uuid;
    private Long projectId;
    private Long processAreaId;
    private String name;
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
