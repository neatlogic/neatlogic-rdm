package codedriver.module.rdm.services;

import codedriver.module.rdm.dao.mapper.ProjectGroupMemberMapper;
import codedriver.module.rdm.dto.ProjectGroupMemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-14 17:05
 **/
@Service
public class ProjectGroupMemberServiceImpl implements ProjectGroupMemberService {

    @Autowired
    private ProjectGroupMemberMapper memberMapper;

    @Override
    public void deleteProjectGroupMember(ProjectGroupMemberVo memberVo) {
        memberMapper.deleteProjectGroupMember(memberVo);
    }

    @Override
    public List<ProjectGroupMemberVo> searchProjectGroupMemberList(String projectUuid) {
        return memberMapper.getProjectGroupMemberList(projectUuid);
    }

    @Override
    public List<Long> saveProjectGroupMember(String groupUuid, List<String> userIdList) {
        ProjectGroupMemberVo member = new ProjectGroupMemberVo();
        member.setGroupUuid(groupUuid);
        memberMapper.deleteProjectGroupMember(member);

        List<Long> idList = new ArrayList<>();
        for (String userId : userIdList){
            ProjectGroupMemberVo memberVo = new ProjectGroupMemberVo();
            memberVo.setUserId(userId);
            memberVo.setGroupUuid(groupUuid);
            memberMapper.insertProjectGroupMember(memberVo);
            idList.add(memberVo.getId());
        }
        return idList;
    }
}
