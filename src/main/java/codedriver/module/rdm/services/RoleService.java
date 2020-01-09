package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.RoleVo;

import java.util.List;

public interface RoleService {

    void saveRole(RoleVo roleVo);

    int getRoleCount(RoleVo roleVo);

    void deleteRoleByUserId(String userId);

    List<RoleVo> searchRole(RoleVo roleVo);
}
