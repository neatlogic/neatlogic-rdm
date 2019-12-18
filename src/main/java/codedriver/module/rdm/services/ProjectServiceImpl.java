package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.util.PageUtil;
import codedriver.module.rdm.constants.ProjectStatusType;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dto.ProjectVo;
import codedriver.module.rdm.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
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

    @Override
    public String saveProject(ProjectVo projectVo) {
        projectVo.setUpdateUser(UserContext.get().getUserId());
        if (StringUtils.isBlank(projectVo.getUuid())){
            projectVo.setUuid(UuidUtil.getUuid());
            projectVo.setStatus(ProjectStatusType.RUN.getName());
            projectVo.setCreateUser(UserContext.get().getUserId());
            projectMapper.insertProject(projectVo);
        }else {
            projectMapper.updateProject(projectVo);
        }
        return projectVo.getUuid();
    }
}
