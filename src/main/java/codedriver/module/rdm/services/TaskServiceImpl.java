package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.module.rdm.dao.mapper.ProjectIterationMapper;
import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dao.mapper.TaskMapper;
import codedriver.module.rdm.dto.*;
import codedriver.module.rdm.exception.projectiteration.ProjectIterationNotExistException;
import codedriver.module.rdm.exception.projectstatus.ProjectStatusNotExistException;
import codedriver.module.rdm.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TaskServiceImpl
 * @Description
 * @Auther
 * @Date 2019/12/24 10:42
 **/
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private ProjectWorkflowMapper projectWorkflowMapper;

    @Resource
    private ProjectIterationMapper projectIterationMapper;

    @Override
    public String saveTask(TaskVo taskVo) {
        String uuid = UuidUtil.getUuid();

        taskVo.setUuid(uuid);
        taskVo.setCreateUser(UserContext.get().getUserId());

        String projectUuid = taskVo.getProjectUuid();
        String iterationUuid = taskVo.getIterationUuid();
        String iterationName = taskVo.getIterationName();
        if(StringUtils.isNotBlank(iterationUuid)){
            ProjectIterationVo iterationVo = projectIterationMapper.getProjectIterationByUuid(projectUuid, iterationUuid);
            if(iterationVo == null){
                throw new ProjectIterationNotExistException(iterationName);
            }
        }

        String statusUuid = taskVo.getStatusUuid();
        String statusName = taskVo.getStatusName();
        String processAreaUuid = taskVo.getProcessAreaUuid();
        if(StringUtils.isNotBlank(statusUuid)){
            ProjectWorkFlowStatusVo statusVo = projectWorkflowMapper.getProjectWorkflowStatus(projectUuid, processAreaUuid, statusUuid);
            if(statusVo == null){
                throw new ProjectStatusNotExistException(statusName);
            }
        }

        taskMapper.insertTask(taskVo);

        if (taskVo.getProcessAccountIdList() != null) {
            List<String> accountIdList = taskVo.getProcessAccountIdList();
            for (String account : accountIdList) {
                taskMapper.insertTaskProcessAccount(uuid, account);
            }
        }

        if (taskVo.getTaskFieldList() != null) {
            List<FieldVo> fieldList = taskVo.getTaskFieldList();
            for (FieldVo field : fieldList) {
                TaskFieldVo taskField = new TaskFieldVo(field);
                taskField.setTaskUuid(uuid);
                taskMapper.insertTaskField(taskField);
            }
        }

        if(StringUtils.isNotBlank(taskVo.getDescription())){
            taskMapper.insertTaskDescription(uuid, taskVo.getDescription());
        }

        return uuid;
    }

    @Override
    public void associateTask(List<TaskAssociateVo> associateList) {
        for(TaskAssociateVo taskAssociateVo : associateList){
            taskMapper.replaceAssociate(taskAssociateVo);
        }
    }

    @Override
    public void deleteAssociate(String taskUuid, String targetUuid) {
        taskMapper.deleteAssociate(taskUuid, targetUuid);
    }

}