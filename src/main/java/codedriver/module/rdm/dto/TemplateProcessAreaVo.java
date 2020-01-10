package codedriver.module.rdm.dto;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-16 17:58
 **/
public class TemplateProcessAreaVo {
    private Long id;

    private String processAreaName;

    private String templateUuid;

    private String processAreaUuid;

    private String processAreaFieldSort;

    private List<TemplateProcessAreaFieldVo> processAreaFieldVoList;

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

    public String getProcessAreaFieldSort() {
        return processAreaFieldSort;
    }

    public void setProcessAreaFieldSort(String processAreaFieldSort) {
        this.processAreaFieldSort = processAreaFieldSort;
    }

    public List<TemplateProcessAreaFieldVo> getProcessAreaFieldVoList() {
        return processAreaFieldVoList;
    }

    public void setProcessAreaFieldVoList(List<TemplateProcessAreaFieldVo> processAreaFieldVoList) {
        this.processAreaFieldVoList = processAreaFieldVoList;
    }

    public String getProcessAreaName() {
        return processAreaName;
    }

    public void setProcessAreaName(String processAreaName) {
        this.processAreaName = processAreaName;
    }
}
