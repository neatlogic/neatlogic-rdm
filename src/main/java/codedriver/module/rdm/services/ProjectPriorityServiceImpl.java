package codedriver.module.rdm.services;

import codedriver.framework.common.util.PageUtil;
import codedriver.module.rdm.dao.mapper.ProjectPriorityMapper;
import codedriver.module.rdm.dto.ProjectPriorityVo;
import codedriver.module.rdm.exception.projectpriority.ProjectPriorityExistException;
import codedriver.module.rdm.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectStatusServiceImpl
 * @Description
 * @Auther
 * @Date 2019/12/13 14:33
 **/

@Service
@Transactional
public class ProjectPriorityServiceImpl implements ProjectPriorityService {

    @Resource
    private ProjectPriorityMapper projectPriorityMapper;

    @Override
    public String saveProjectPriority(ProjectPriorityVo projectPriorityVo) {
        String uuid;

        int count = projectPriorityMapper.checkProjectPriorityExist(projectPriorityVo);
        if(count >= 1){
            throw new ProjectPriorityExistException(projectPriorityVo.getName());
        }

        if(StringUtils.isNotBlank(projectPriorityVo.getUuid())){
            uuid = projectPriorityVo.getUuid();
            projectPriorityMapper.updateProjectPriority(projectPriorityVo);
        }else{
            uuid = UuidUtil.getUuid();
            projectPriorityVo.setUuid(uuid);
            projectPriorityMapper.insertProjectPriority(projectPriorityVo);
        }

        return uuid;
    }

    @Override
    public List<ProjectPriorityVo> searchProjectPriority(ProjectPriorityVo projectPriorityVo) {
        if (projectPriorityVo.getNeedPage()) {
            int rowNum = projectPriorityMapper.searchProjectPriorityCount(projectPriorityVo);
            projectPriorityVo.setRowNum(rowNum);
            projectPriorityVo.setPageCount(PageUtil.getPageCount(rowNum, projectPriorityVo.getPageSize()));
        }
        return projectPriorityMapper.searchProjectPriority(projectPriorityVo);
    }
}
