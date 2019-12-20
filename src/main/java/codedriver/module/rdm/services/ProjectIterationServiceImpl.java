package codedriver.module.rdm.services;

import codedriver.module.rdm.dao.mapper.ProjectIterationMapper;
import codedriver.module.rdm.dto.ProjectIterationVo;
import codedriver.module.rdm.dto.ProjectPriorityVo;
import codedriver.module.rdm.exception.projectpriority.ProjectPriorityExistException;
import codedriver.module.rdm.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectIterationServiceImpl
 * @Description
 * @Auther
 * @Date 2019/12/18 16:02
 **/

@Service
@Transactional
public class ProjectIterationServiceImpl implements ProjectIterationService{

    @Resource
    private ProjectIterationMapper projectIterationMapper;

    @Override
    public String saveProjectIteration(ProjectIterationVo projectIterationVo) {
        String uuid;

        int count = projectIterationMapper.checkProjectIterationExist(projectIterationVo);
        if(count >= 1){
            throw new ProjectPriorityExistException(projectIterationVo.getName());
        }

        if(StringUtils.isNotBlank(projectIterationVo.getUuid())){
            uuid = projectIterationVo.getUuid();
            projectIterationMapper.updateProjectIteration(projectIterationVo);
        }else{
            uuid = UuidUtil.getUuid();
            projectIterationVo.setUuid(uuid);
            projectIterationMapper.insertProjectIteration(projectIterationVo);
        }

        return uuid;
    }

    @Override
    public List<ProjectIterationVo> searchProjectIteration(ProjectIterationVo projectIterationVo) {
        return null;
    }
}
