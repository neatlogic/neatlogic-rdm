package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectGroupVo;

public interface ProjectGroupMapper {

    void insertProjectGroup(ProjectGroupVo groupVo);

    void updateProjectGroup(ProjectGroupVo groupVo);

    void deleteProjectGroup(ProjectGroupVo groupVo);
}
