package codedriver.module.rdm.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-19 11:01
 **/
public class TemplateProcessAreaTemplateVo {
    private Long id;
    private String templateUuid;
    private String processAreaUuid;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateUuid() {
        return templateUuid;
    }

    public void setTemplateUuid(String templateUuid) {
        this.templateUuid = templateUuid;
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
