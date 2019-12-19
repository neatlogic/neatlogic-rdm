package codedriver.module.rdm.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-19 14:35
 **/
public class ProjectProcessAreaVo {

    private Long id;
    private String projectUuid;
    private String processAreaUuid;
    private String processAreaName;
    private String processAreaSort;
    private int isEnable;

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

    public String getProcessAreaSort() {
        return processAreaSort;
    }

    public void setProcessAreaSort(String processAreaSort) {
        this.processAreaSort = processAreaSort;
    }
}
