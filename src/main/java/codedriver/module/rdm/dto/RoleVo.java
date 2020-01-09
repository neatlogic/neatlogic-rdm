package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 10:19
 **/
public class RoleVo extends BasePageVo {
    @EntityField( name = "主键ID", type = ApiParamType.LONG)
    private Long id;

    @EntityField( name = "用户ID", type = ApiParamType.STRING)
    private String userId;

    @EntityField( name = "用户名称", type = ApiParamType.STRING)
    private String userName;

    @EntityField( name = "用户角色", type = ApiParamType.STRING)
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
