package codedriver.module.rdm.services;

import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dto.ProjectWorkFlowStatusVo;
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
    public void saveProjectWorkFlow(String projectUuid, String processAreaUuid, List<ProjectWorkFlowStatusVo> statusList) {
        projectWorkflowMapper.deleteAllProjectStatus(projectUuid, processAreaUuid);
        for (ProjectWorkFlowStatusVo projectWorkFlowStatusVo : statusList) {
            projectWorkflowMapper.insertProjectWorkflowStatus(projectWorkFlowStatusVo);
            List<String> transferList = projectWorkFlowStatusVo.getTransferTo();
            for (String transferTo : transferList) {
                projectWorkflowMapper.insertProjectWorkflowStatusTransfer(projectWorkFlowStatusVo.getUuid(), transferTo);
            }
        }

    }


    @Override
    public String saveProjectStatus(ProjectWorkFlowStatusVo projectWorkFlowStatusVo) {
        String uuid;

        int count = projectWorkflowMapper.checkProjectStatusExist(projectWorkFlowStatusVo);
        if (count >= 1) {
            throw new ProjectStatusExistException(projectWorkFlowStatusVo.getName());
        }

        if (StringUtils.isNotBlank(projectWorkFlowStatusVo.getUuid())) {
            uuid = projectWorkFlowStatusVo.getUuid();
            projectWorkflowMapper.updateProjectStatus(projectWorkFlowStatusVo);
        } else {
            uuid = UuidUtil.getUuid();
            projectWorkFlowStatusVo.setUuid(uuid);
            projectWorkflowMapper.insertProjectWorkflowStatus(projectWorkFlowStatusVo);
        }

        return uuid;
    }
}
