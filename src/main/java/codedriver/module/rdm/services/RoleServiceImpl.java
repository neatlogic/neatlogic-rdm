package codedriver.module.rdm.services;

import codedriver.module.rdm.dao.mapper.RoleMapper;
import codedriver.module.rdm.dto.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 10:43
 **/
@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void saveRole(RoleVo roleVo) {
        roleMapper.insertRole(roleVo);
    }

    @Override
    public int getRoleCount(RoleVo roleVo) {
        return roleMapper.selectRoleCount(roleVo);
    }


    @Override
    public void deleteRoleByUserId(String userId) {
        roleMapper.deleteRoleByUserId(userId);
    }

    @Override
    public List<RoleVo> searchRole(RoleVo roleVo) {
        return roleMapper.searchRole(roleVo);
    }
}
