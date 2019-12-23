package codedriver.module.rdm.dto;

import codedriver.framework.dto.UserVo;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-23 18:25
 **/
public class ProjectMemberVo {
    private Long id;
    private String projectUuid;
    private String userId;
    private UserVo userVo;

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
}
