package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.RoleVo;

public interface RoleService {

    void saveRole(RoleVo roleVo);

    int getRoleCount(RoleVo roleVo);

    void deleteRoleByUserId(String userId);
}
