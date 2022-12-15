/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.dto.UserVo;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-23 18:25
 **/
public class ProjectGroupMemberVo extends BasePageVo {

    @EntityField(name = "主键ID", type = ApiParamType.LONG)
    private Long id;

    @EntityField(name = "用户id", type = ApiParamType.STRING)
    private String userId;

    @EntityField(name = "项目组uuid", type = ApiParamType.LONG)
    private String groupUuid;

    private UserVo userVo;

    private ProjectGroupVo groupVo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    public ProjectGroupVo getGroupVo() {
        return groupVo;
    }

    public void setGroupVo(ProjectGroupVo groupVo) {
        this.groupVo = groupVo;
    }
}
