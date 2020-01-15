package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectGroupMemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectGroupMemberMapper {

    List<ProjectGroupMemberVo> getProjectGroupMemberList(String projectUuid);

    void insertProjectGroupMember(ProjectGroupMemberVo memberVo);

    void deleteProjectGroupMember(ProjectGroupMemberVo memberVo);

    List<ProjectGroupMemberVo> getProjectGroupMember(@Param("projectUuid") String projectUuid, @Param("userId") String userId);
}
