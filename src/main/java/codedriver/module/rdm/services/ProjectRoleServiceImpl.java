package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.module.rdm.annotation.RoleAction;
import codedriver.module.rdm.dao.mapper.ProjectRoleMapper;
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
@RoleAction
@Service
public class ProjectRoleServiceImpl implements ProjectRoleService {

    @Autowired
    private ProjectRoleMapper roleMapper;

    @Override
    public void saveProjectRoleAction(Long groupId, List<Long> actionIdList) {
        roleMapper.deleteProjectRoleAction(groupId);
        for (Long actionId : actionIdList){
            RoleActionVo actionVo = new RoleActionVo();
            actionVo.setActionId(actionId);
            actionVo.setGroupId(groupId);
            actionVo.setCreateUser(UserContext.get().getUserId());
            roleMapper.insertProjectRoleAction(actionVo);
        }
    }

    @Override
    public List<RoleActionVo> searchProjectRoleAction(Long groupId, String module) {
        return roleMapper.searchRoleActionByGroupIdAndModule(groupId, module);
    }
}
