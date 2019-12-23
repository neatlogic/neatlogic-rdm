package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-19 14:35
 **/
public class ProjectProcessAreaVo extends BasePageVo {

    @EntityField(name = "主键ID", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "项目uuid", type = ApiParamType.STRING)
    private String projectUuid;
    @EntityField(name = "过程域uuid", type = ApiParamType.STRING)
    private String processAreaUuid;
    @EntityField(name = "过程域名称", type = ApiParamType.STRING)
    private String processAreaName;
    @EntityField(name = "过程域属性排序", type = ApiParamType.STRING)
    private String processAreaFieldSort;
    @EntityField(name = "是否显示", type = ApiParamType.INTEGER)
    private int isEnable;
    private List<ProjectProcessAreaFieldVo> fieldList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public String getProcessAreaName() {
        return processAreaName;
    }

    public void setProcessAreaName(String processAreaName) {
        this.processAreaName = processAreaName;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    public String getProcessAreaFieldSort() {
        return processAreaFieldSort;
    }

    public void setProcessAreaFieldSort(String processAreaFieldSort) {
        this.processAreaFieldSort = processAreaFieldSort;
    }

    public List<ProjectProcessAreaFieldVo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<ProjectProcessAreaFieldVo> fieldList) {
        this.fieldList = fieldList;
    }
}
