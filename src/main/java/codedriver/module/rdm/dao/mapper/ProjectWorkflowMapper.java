package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectStatusVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ProjectWorkFlowMapper
 * @Description
 * @Auther
 * @Date 2019/12/13 14:40
 **/
public interface ProjectWorkflowMapper {

    void insertProjectWorkflowStatus(ProjectStatusVo projectStatusVo);

    void insertProjectWorkflowStatusTransfer(@Param("uuid")String uuid, @Param("transferTo")String transferTo);

    void deleteAllProjectStatus(@Param("projectUuid") String projectUuid, @Param("processAreaUuid")String processAreaUuid);

    List<ProjectStatusVo> getProjectWorkFlow(@Param("projectUuid")String projectUuid, @Param("processAreaUuid")String processAreaUuid);

    List<ProjectStatusVo> getTransferStatusList(@Param("projectUuid")String projectUuid, @Param("processAreaUuid")String processAreaUuid, @Param("statusUuid")String statusUuid);

    ProjectStatusVo getProjectWorkflowStatus(@Param("projectUuid") String projectUuid, @Param("processAreaUuid")String processAreaUuid,@Param("uuid") String uuid);

    void deleteProjectStatusByUuid(@Param("projectUuid") String projectUuid, @Param("processAreaUuid")String processAreaUuid, @Param("uuid") String uuid);

    int checkProjectStatusExist(ProjectStatusVo projectStatusVo);

    void updateProjectStatus(ProjectStatusVo projectStatusVo);

}
