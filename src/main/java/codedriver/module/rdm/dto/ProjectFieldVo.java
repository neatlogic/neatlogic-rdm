package codedriver.module.rdm.dto;

/**
 * @ClassName ProjectFieldVo
 * @Description 项目属性类
 * @Auther
 * @Date 2019/12/4 15:58
 **/
public class ProjectFieldVo extends FieldVo {

    private String projectUuid;
    private String processAreaUuid;

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
}
