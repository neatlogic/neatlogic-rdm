package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-16 17:58
 **/
public class TemplateProcessAreaVo {
    private Long id;

    private String templateUuid;

    private String processAreaUuid;

    private String processAreaFieldSort;

    List<TemplateProcessAreaFieldVo> processAreaFieldVoList;

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
}
