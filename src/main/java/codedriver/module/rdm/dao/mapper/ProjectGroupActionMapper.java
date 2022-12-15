package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectGroupActionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectGroupActionMapper {
    public void insertProjectGroupAction(ProjectGroupActionVo roleActionVo);

    public void insertProjectGroupActionProcess(@Param("groupActionId") Long groupActionId, @Param("processAreaUuid") String processAreaUuid);

    public void deleteProjectGroupAction(String groupUuid);

    public List<ProjectGroupActionVo> searchGroupActionByParams(ProjectGroupActionVo actionVo);

    public void deleteGroupActionProcessArea(Long groupActionId);


}