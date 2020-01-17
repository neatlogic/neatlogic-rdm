package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectWorkFlowStatusVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ProjectWorkFlowMapper
 * @Description
 * @Auther
 * @Date 2019/12/13 14:40
 **/
public interface ProjectWorkflowMapper {

    void insertProjectWorkflowStatus(ProjectWorkFlowStatusVo projectWorkFlowStatusVo);

    void insertProjectWorkflowStatusTransfer(@Param("uuid") String uuid, @Param("transferTo") String transferTo);

    void deleteAllProjectStatus(@Param("projectUuid") String projectUuid, @Param("processAreaUuid") String processAreaUuid);

    List<ProjectWorkFlowStatusVo> getProjectWorkFlow(@Param("projectUuid") String projectUuid, @Param("processAreaUuid") String processAreaUuid);

    List<ProjectWorkFlowStatusVo> getTransferStatusList(@Param("projectUuid") String projectUuid, @Param("processAreaUuid") String processAreaUuid, @Param("statusUuid") String statusUuid);

    ProjectWorkFlowStatusVo getProjectWorkflowStatus(@Param("projectUuid") String projectUuid, @Param("processAreaUuid") String processAreaUuid, @Param("uuid") String uuid);

    void deleteProjectStatusByUuid(@Param("projectUuid") String projectUuid, @Param("processAreaUuid") String processAreaUuid, @Param("uuid") String uuid);

    int checkProjectStatusExist(ProjectWorkFlowStatusVo projectWorkFlowStatusVo);

    void updateProjectStatus(ProjectWorkFlowStatusVo projectWorkFlowStatusVo);

}
