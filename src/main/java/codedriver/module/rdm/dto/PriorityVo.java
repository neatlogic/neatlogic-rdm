package codedriver.module.rdm.dto;

/**
 * @ClassName PriorityVo
 * @Description 优先级类
 * @Auther
 * @Date 2019/12/4 18:23
 **/
public class PriorityVo {
    private Long id;
    private String uuid;
    private String name;
    private String color;
    private Long processAreaId;
    private String processAreaUuid;
    private Long projectId;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
}
