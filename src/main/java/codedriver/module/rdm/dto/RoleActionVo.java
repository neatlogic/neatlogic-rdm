package codedriver.module.rdm.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 11:08
 **/
public class RoleActionVo {
    private Long id;
    private Long groupId;
    private Long actionId;
    private String createUser;
    private String createTime;
    private ActionVo actionVo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
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

    public ActionVo getActionVo() {
        return actionVo;
    }

    public void setActionVo(ActionVo actionVo) {
        this.actionVo = actionVo;
    }
}
