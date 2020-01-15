package codedriver.module.rdm.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-13 14:40
 **/
public class ActionCheckVo {
    private String projectUuid;
    private String module;
    private String userId;
    private String processAreaUuid;

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }
}
