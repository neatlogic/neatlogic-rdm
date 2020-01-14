package codedriver.module.rdm.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 11:08
 **/
public class ProjectGroupActionVo {
    private Long id;
    private String groupUuid;
    private String action;
    private String module;
    private String createUser;
    private String createTime;
    private String processAreaUuid;

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
