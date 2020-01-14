package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.RoleActionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectRoleMapper {
    public void insertProjectRoleAction(RoleActionVo roleActionVo);

    public void deleteProjectRoleAction(String groupUuid);

    public List<RoleActionVo> searchRoleActionByGroupUuidAndModule(@Param("groupUuid") String groupUuid, @Param("module") String module);
}
