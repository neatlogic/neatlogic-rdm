package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.RoleActionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectRoleMapper {
    public void insertProjectRoleAction(RoleActionVo roleActionVo);

    public void deleteProjectRoleAction(Long groupId);

    public List<RoleActionVo> searchRoleActionByGroupIdAndModule(@Param("groupId") Long groupId, @Param("module") String module);

    public int checkRoleAction(@Param("projectUuid") String projectUuid, @Param("userId") String userId, @Param("module")String module);
}
