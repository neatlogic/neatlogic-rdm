package codedriver.module.rdm.dto;

import java.util.List;

/**
 * @ClassName StatusVo
 * @Description
 * @Auther
 * @Date 2019/12/4 18:23
 **/
public class ProjectStatusVo {
    private Long id;
    private String uuid;
    private String name;
    private Long processAreaId;
    private String processAreaUuid;
    private Long projectId;
    private String projectUuid;

    public List<Long> transferTo;

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

    public List<Long> getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(List<Long> transferTo) {
        this.transferTo = transferTo;
    }
}
