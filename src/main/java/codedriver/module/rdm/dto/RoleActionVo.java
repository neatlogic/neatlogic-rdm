package codedriver.module.rdm.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 11:08
 **/
public class RoleActionVo {
    private Long id;
    private Long groupUuid;
    private String action;
    private String module;
    private String createUser;
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(Long groupUuid) {
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
