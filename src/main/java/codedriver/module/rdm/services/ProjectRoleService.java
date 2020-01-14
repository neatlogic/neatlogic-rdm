package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ActionCheckVo;
import codedriver.module.rdm.dto.RoleActionVo;

import java.util.List;

public interface ProjectRoleService {

    public void saveProjectRoleAction(String groupUuId, List<RoleActionVo> roleActionVoList);

    public List<RoleActionVo> searchProjectRoleAction(String groupUuid, String module);

    public void test(ActionCheckVo actionCheckVo);

}
