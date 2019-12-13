package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectStatusVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ProjectWorkFlowMapper
 * @Description
 * @Auther fandong
 * @Date 2019/12/13 14:40
 **/
public interface ProjectWorkflowMapper {

    void insertProjectWorkflowStatus(ProjectStatusVo projectStatusVo);

    void insertProjectWorkflowStatusTransfer(@Param("uuid")String uuid, @Param("transferTo")String transferTo);

    void deleteAllProjectStatus(@Param("projectUuid") String projectUuid, @Param("processAreaUuid")String processAreaUuid);

    List<ProjectStatusVo> getProjectWorkFlow(@Param("projectUuid")String projectUuid, @Param("processAreaUuid")String processAreaUuid);
}
