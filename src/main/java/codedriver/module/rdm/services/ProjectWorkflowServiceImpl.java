package codedriver.module.rdm.services;

import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dto.ProjectStatusVo;
import codedriver.module.rdm.exception.projectstatus.ProjectStatusExistException;
import codedriver.module.rdm.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectStatusServiceImpl
 * @Description
 * @Auther
 * @Date 2019/12/13 14:33
 **/

@Service
@Transactional
public class ProjectWorkflowServiceImpl implements ProjectWorkflowService {

    @Resource
    private ProjectWorkflowMapper projectWorkflowMapper;

    @Override
    public void saveProjectWorkFlow(String projectUuid, String processAreaUuid, List<ProjectStatusVo> statusList) {
        projectWorkflowMapper.deleteAllProjectStatus(projectUuid, processAreaUuid);
        for(ProjectStatusVo projectStatusVo : statusList){
            projectWorkflowMapper.insertProjectWorkflowStatus(projectStatusVo);
            List<String> transferList = projectStatusVo.getTransferTo();
            for(String transferTo : transferList){
                projectWorkflowMapper.insertProjectWorkflowStatusTransfer(projectStatusVo.getUuid(), transferTo);
            }
        }

    }


    @Override
    public String saveProjectStatus(ProjectStatusVo projectStatusVo) {
        String uuid;

        int count = projectWorkflowMapper.checkProjectStatusExist(projectStatusVo);
        if(count >= 1){
            throw new ProjectStatusExistException(projectStatusVo.getName());
        }

        if(StringUtils.isNotBlank(projectStatusVo.getUuid())){
            uuid = projectStatusVo.getUuid();
            projectWorkflowMapper.updateProjectStatus(projectStatusVo);
        }else{
            uuid = UuidUtil.getUuid();
            projectStatusVo.setUuid(uuid);
            projectWorkflowMapper.insertProjectWorkflowStatus(projectStatusVo);
        }

        return uuid;
    }
}
