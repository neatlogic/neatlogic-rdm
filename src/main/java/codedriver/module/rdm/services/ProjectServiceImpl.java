package codedriver.module.rdm.services;

import codedriver.framework.common.util.PageUtil;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dto.ProjectVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectServiceImpl
 * @Description
 * @Auther fandong
 * @Date 2019/12/10 17:11
 **/
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;
    @Override
    public List<ProjectVo> searchProject(ProjectVo projectVo) {
        if (projectVo.getNeedPage()) {
            int rowNum = projectMapper.searchProjectCount(projectVo);
            projectVo.setRowNum(rowNum);
            projectVo.setPageCount(PageUtil.getPageCount(rowNum, projectVo.getPageSize()));
        }
        return projectMapper.searchProject(projectVo);
    }
}
