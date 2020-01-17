package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ProjectGroupMemberVo;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-14 17:04
 **/
public interface ProjectGroupMemberService {

    void deleteProjectGroupMember(ProjectGroupMemberVo memberVo);

    List<ProjectGroupMemberVo> searchProjectGroupMemberList(String projectUuid);

    List<Long> saveProjectGroupMember(String groupUuid, List<String> userIdList);
}
