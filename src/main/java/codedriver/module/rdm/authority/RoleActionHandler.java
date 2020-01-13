package codedriver.module.rdm.authority;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.module.rdm.annotation.ActionCheck;
import codedriver.module.rdm.dao.mapper.ProjectMemberMapper;
import codedriver.module.rdm.dao.mapper.ProjectRoleMapper;
import codedriver.module.rdm.dto.ActionCheckVo;
import codedriver.module.rdm.dto.ProjectMemberVo;
import codedriver.module.rdm.dto.RoleActionVo;
import codedriver.module.rdm.exception.role.ActionCheckFailedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-13 16:56
 **/
@Aspect
@Service
public class RoleActionHandler {

    @Autowired
    private ProjectMemberMapper memberMapper;

    @Autowired
    private ProjectRoleMapper roleMapper;

    @Before("@annotation(actionCheck)")
    public void ActionCheck(JoinPoint point, ActionCheck actionCheck){
        Object[] params = point.getArgs();
        ActionCheckVo checkVo = (ActionCheckVo)params[0];
        ProjectMemberVo memberVo = memberMapper.getProjectMember(checkVo.getProjectUuid(), checkVo.getUserId());
        List<RoleActionVo> actionVoList = roleMapper.searchRoleActionByGroupIdAndModule(memberVo.getGroupId(), checkVo.getModule());
        boolean auth = false;
        for (RoleActionVo actionVo : actionVoList){
            if (actionCheck.value().equals(actionVo.getActionVo().getName())){
                auth = true;
                break;
            }
        }
        if (!auth){
            throw new ActionCheckFailedException();
        }
    }

}
