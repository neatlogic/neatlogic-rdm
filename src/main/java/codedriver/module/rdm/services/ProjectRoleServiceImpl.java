package codedriver.module.rdm.services;

import codedriver.module.rdm.annotation.ActionCheck;
import codedriver.module.rdm.annotation.InputParam;
import codedriver.module.rdm.dao.mapper.ProjectRoleMapper;
import codedriver.module.rdm.dto.ActionCheckVo;
import codedriver.module.rdm.dto.RoleActionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 11:31
 **/
@Transactional
@Service
public class ProjectRoleServiceImpl implements ProjectRoleService {

    @Autowired
    private ProjectRoleMapper roleMapper;

    @Override
    public void saveProjectRoleAction(Long groupId, List<RoleActionVo> roleActionVoList) {
        roleMapper.deleteProjectRoleAction(groupId);
        for (RoleActionVo actionVo : roleActionVoList){
            roleMapper.insertProjectRoleAction(actionVo);
        }
    }

    @Override
    public List<RoleActionVo> searchProjectRoleAction(Long groupId, String module) {
        return roleMapper.searchRoleActionByGroupIdAndModule(groupId, module);
    }

    @ActionCheck(name = "检查", value = "check")
    @Override
    public void test(@InputParam ActionCheckVo actionCheckVo) {
        System.out.println("aa");
    }
}
