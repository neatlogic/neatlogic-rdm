package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectGroupMemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectGroupMemberMapper {

    List<ProjectGroupMemberVo> getProjectMemberList(String projectUuid);

    void insertProjectMember(ProjectGroupMemberVo memberVo);

    void deleteProjectMember(ProjectGroupMemberVo memberVo);

    ProjectGroupMemberVo getProjectMember(@Param("projectUuid") String projectUuid, @Param("userId") String userId);
}
