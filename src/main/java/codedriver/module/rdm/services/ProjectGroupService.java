package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ProjectGroupVo;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 15:14
 **/
public interface ProjectGroupService {

    void saveProjectGroup(ProjectGroupVo groupVo);

    void deleteProjectGroup(ProjectGroupVo groupVo);
}
