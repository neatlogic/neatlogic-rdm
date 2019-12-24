package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectMemberVo;

import java.util.List;

public interface ProjectMemberMapper {

    List<ProjectMemberVo> getProjectMemberList(String projectUuid);

    void insertProjectMember(ProjectMemberVo memberVo);

    void updateProjectMember(ProjectMemberVo memberVo);

    void deleteProjectMemberByUserId(String userId);
}
