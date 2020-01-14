package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ActionCheckVo;
import codedriver.module.rdm.dto.ProjectGroupActionVo;

import java.util.List;

public interface ProjectRoleService {

    public void saveProjectRoleAction(String groupUuId, List<ProjectGroupActionVo> roleActionVoList);

    public List<ProjectGroupActionVo> searchProjectRoleAction(String groupUuid, String module);

    public void test(ActionCheckVo actionCheckVo);

}
