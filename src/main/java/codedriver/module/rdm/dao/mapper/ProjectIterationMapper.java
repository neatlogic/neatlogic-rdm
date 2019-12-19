package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.ProjectIterationVo;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName ProjectIterationMapper
 * @Description
 * @Auther
 * @Date 2019/12/18 14:24
 **/
public interface ProjectIterationMapper {
    ProjectIterationVo getProjectIterationByUuid(@Param("projectUuid")  String projectUuid, @Param("uuid")  String uuid);

    void deleteProjectIteration(@Param("projectUuid")  String projectUuid, @Param("uuid")  String uuid);

    int checkProjectIterationExist(ProjectIterationVo projectIterationVo);

    void updateProjectIteration(ProjectIterationVo projectIterationVo);

    void insertProjectIteration(ProjectIterationVo projectIterationVo);
}
