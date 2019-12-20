package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ProjectMapper
 * @Description
 * @Auther
 * @Date 2019/12/10 17:11
 **/
public interface ProjectMapper {
    int searchProjectCount(ProjectVo projectVo);

    int insertProject(ProjectVo projectVo);

    int updateProject(ProjectVo projectVo);

    List<ProjectVo> searchProject(ProjectVo projectVo);

    ProjectVo getProjectByUuid(String uuid);

    List<FieldVo> getProjectFieldList(@Param("projectUuid") String projectUuid, @Param("processAreaUuid") String processAreaUuid);

    void insertProjectProcessArea(ProjectProcessAreaVo processAreaVo);

    void insertProjectProcessAreaField(ProjectProcessAreaFieldVo processAreaFieldVo);

    void insertProjectProcessAreaTemplate(ProjectProcessAreaTemplateVo templateVo);
}
