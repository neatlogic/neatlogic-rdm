package codedriver.module.rdm.authority;

import codedriver.module.rdm.annotation.ActionCheck;
import codedriver.module.rdm.dao.mapper.ProjectGroupMemberMapper;
import codedriver.module.rdm.dao.mapper.ProjectGroupActionMapper;
import codedriver.module.rdm.dto.ActionCheckVo;
import codedriver.module.rdm.dto.ProjectGroupMemberVo;
import codedriver.module.rdm.dto.ProjectGroupActionVo;
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
    private ProjectGroupMemberMapper memberMapper;

    @Autowired
    private ProjectGroupActionMapper roleMapper;

    @Before("@annotation(actionCheck)")
    public void ActionCheck(JoinPoint point, ActionCheck actionCheck){
        ActionCheckVo checkVo = new ActionCheckVo();
        Object[] params = point.getArgs();
        for (Object param : params){
            if (param instanceof ActionCheckVo){
                checkVo = (ActionCheckVo) param;
            }
        }

        boolean auth = false;
        List<ProjectGroupMemberVo> memberVoList = memberMapper.getProjectGroupMember(checkVo.getProjectUuid(), checkVo.getUserId());
        if (memberVoList != null && memberVoList.size() > 0){
            for (ProjectGroupMemberVo member : memberVoList){
                ProjectGroupActionVo actionParam = new ProjectGroupActionVo();
                actionParam.setGroupUuid(member.getGroupUuid());
                actionParam.setModule(checkVo.getModule());
                actionParam.setProcessAreaUuid(checkVo.getProcessAreaUuid());
                List<ProjectGroupActionVo> actionVoList = roleMapper.searchGroupActionByParams(actionParam);
                for (ProjectGroupActionVo actionVo : actionVoList){
                    if (actionCheck.value().equals(actionVo.getAction())){
                        auth = true;
                        break;
                    }
                }
            }
        }
        if (!auth){
            throw new ActionCheckFailedException();
        }
    }

}
