package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectPriorityVo;
import codedriver.module.rdm.dto.ProjectStatusVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ProjectWorkFlowMapper
 * @Description
 * @Auther
 * @Date 2019/12/13 14:40
 **/
public interface ProjectPriorityMapper {

    int checkProjectPriorityExist(ProjectPriorityVo projectPriorityVo);

    void updateProjectPriority(ProjectPriorityVo projectPriorityVo);

    void insertProjectPriority(ProjectPriorityVo projectPriorityVo);

    ProjectPriorityVo getProjectPriorityByUuid(String uuid);

    int searchProjectPriorityCount(ProjectPriorityVo projectPriorityVo);

    List<ProjectPriorityVo> searchProjectPriority(ProjectPriorityVo projectPriorityVo);

    void deleteProjectPriority(String uuid);
}
