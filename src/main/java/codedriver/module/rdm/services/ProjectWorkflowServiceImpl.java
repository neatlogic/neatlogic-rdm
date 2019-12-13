package codedriver.module.rdm.services;

import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dto.ProjectStatusVo;
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
}
