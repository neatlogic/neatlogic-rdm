package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.dto.UserVo;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-23 18:25
 **/
public class ProjectMemberVo extends BasePageVo {

    @EntityField(name = "主键ID", type = ApiParamType.LONG)
    private Long id;

    @EntityField(name = "项目Uuid", type = ApiParamType.STRING)
    private String projectUuid;

    @EntityField(name = "用户id", type = ApiParamType.STRING)
    private String userId;
    private UserVo userVo;

    @EntityField(name = "是否为组长", type = ApiParamType.INTEGER)
    private int isLeader;

    @EntityField(name = "项目组id", type = ApiParamType.LONG)
    private Long groupId;
    private ProjectGroupVo groupVo;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public int getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(int isLeader) {
        this.isLeader = isLeader;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public ProjectGroupVo getGroupVo() {
        return groupVo;
    }

    public void setGroupVo(ProjectGroupVo groupVo) {
        this.groupVo = groupVo;
    }
}
