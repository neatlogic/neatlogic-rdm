package codedriver.module.rdm.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-19 18:36
 **/
public class ProjectProcessAreaTemplateVo {
    private String processAreaUuid;
    private String projectUuid;
    private String content;
    private Long id;

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
