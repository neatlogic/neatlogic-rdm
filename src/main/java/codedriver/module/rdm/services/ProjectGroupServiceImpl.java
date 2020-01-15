package codedriver.module.rdm.services;

import codedriver.module.rdm.dao.mapper.ProjectGroupMapper;
import codedriver.module.rdm.dto.ProjectGroupVo;
import codedriver.module.rdm.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 15:14
 **/
@Transactional
@Service
public class ProjectGroupServiceImpl implements ProjectGroupService {

    @Autowired
    private ProjectGroupMapper groupMapper;

    @Override
    public void saveProjectGroup(ProjectGroupVo groupVo) {
        if (groupVo.getId() != null && groupVo.getId() != 0L){
            groupMapper.updateProjectGroup(groupVo);
        }else {
            groupVo.setUuid(UuidUtil.getUuid());
            groupMapper.insertProjectGroup(groupVo);
        }
    }

    @Override
    public void deleteProjectGroup(ProjectGroupVo groupVo) {
        groupMapper.deleteProjectGroup(groupVo);
    }
}
