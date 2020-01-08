package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectMemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMemberMapper {

    List<ProjectMemberVo> getProjectMemberList(String projectUuid);

    void insertProjectMember(ProjectMemberVo memberVo);

    void deleteProjectMember(ProjectMemberVo memberVo);

    ProjectMemberVo getProjectMember(@Param("projectUuid") String projectUuid, @Param("userId") String userId);
}
