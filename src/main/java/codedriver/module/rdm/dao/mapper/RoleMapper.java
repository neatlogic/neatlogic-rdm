package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.RoleVo;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 10:39
 **/
public interface RoleMapper {

    void insertRole(RoleVo roleVo);

    int selectRoleCount(RoleVo roleVo);

    int deleteRoleByUserId(String userId);
}
