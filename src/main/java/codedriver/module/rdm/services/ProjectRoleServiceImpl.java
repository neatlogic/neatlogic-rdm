package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.module.rdm.dao.mapper.ProjectGroupMapper;
import codedriver.module.rdm.dao.mapper.ProjectMemberMapper;
import codedriver.module.rdm.dao.mapper.ProjectRoleMapper;
import codedriver.module.rdm.dto.ProjectMemberVo;
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
    
    @Autowired
    private ProjectMemberMapper memberMapper;

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

    /** 
    * @Description: 项目Uuid, 操作名称，操作所属模块（过程域/知识库等） 
    * @Param: [projectUuid, actionName, module] 
    * @return: boolean  
    */ 
    @Override
    public boolean checkUserActionRole(String projectUuid, String actionName, String module) {
        ProjectMemberVo memberVo = memberMapper.getProjectMember(projectUuid, UserContext.get().getUserId());
        List<RoleActionVo> actionVoList = roleMapper.searchRoleActionByGroupIdAndModule(memberVo.getGroupId(), module);
        for (RoleActionVo actionVo : actionVoList){
            if (actionName.equals(actionVo.getActionVo().getName())){
                return true;
            }
        }
        return false;
    }
}
