package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.RoleActionVo;

import java.util.List;

public interface ProjectRoleService {

    public void saveProjectRoleAction(Long groupId, List<Long> actionIdList);

    public List<RoleActionVo> searchProjectRoleAction(Long groupId, String module);

}
