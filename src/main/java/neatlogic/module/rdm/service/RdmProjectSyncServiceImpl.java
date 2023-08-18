package neatlogic.module.rdm.service;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.dto.plugin.issue.ProjectSearchVo;
import neatlogic.framework.dto.plugin.issue.ProjectVo;
import neatlogic.framework.plugin.issue.ProjectSyncService;
import neatlogic.framework.rdm.dto.ProjectConditionVo;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class RdmProjectSyncServiceImpl implements ProjectSyncService {
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getSource() {
        return "rdm";
    }

    @Override
    public List<ProjectVo> doSync(ProjectSearchVo projectSearchVo) {
        List<ProjectVo> list = new ArrayList<>();
        ProjectConditionVo projectConditionVo = new ProjectConditionVo();
        projectConditionVo.setUserIdList(new ArrayList<String>() {{
            this.add(UserContext.get().getUserUuid(true));
        }});
        List<Long> idList = projectMapper.searchProjectId(projectConditionVo);
        if (!CollectionUtils.isEmpty(idList)) {
            projectConditionVo.setIdList(idList);
            List<neatlogic.framework.rdm.dto.ProjectVo> projectList = projectMapper.searchProject(projectConditionVo);
            if (projectList != null) {
                for (neatlogic.framework.rdm.dto.ProjectVo vo : projectList) {
                    ProjectVo projectVo = new ProjectVo();
                    projectVo.setProjectKey(String.valueOf(vo.getId()));
                    projectVo.setProjectName(vo.getName());
                    list.add(projectVo);
                }
            }
        }
        return list;
    }

    @Override
    public JSONObject getQueryParameter(Long id) {
        return null;
    }
}
