package codedriver.module.rdm.services;

import codedriver.module.rdm.annotation.ActionCheck;
import codedriver.module.rdm.annotation.InputParam;
import codedriver.module.rdm.constants.ModuleType;
import codedriver.module.rdm.dao.mapper.ProjectGroupActionMapper;
import codedriver.module.rdm.dto.ActionCheckVo;
import codedriver.module.rdm.dto.ProjectGroupActionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 11:31
 **/
@Transactional
@Service
public class ProjectRoleServiceImpl implements ProjectRoleService {

    @Autowired
    private ProjectGroupActionMapper groupActionMapper;

    @Override
    public void saveProjectRoleAction(String groupUuid, List<ProjectGroupActionVo> roleActionVoList) {
        List<ProjectGroupActionVo> actionVoList = groupActionMapper.searchGroupActionByGroupUuidAndModule(groupUuid, ModuleType.PROCESS.getValue());
        List<Long> groupActionIdList = new ArrayList<>();
        actionVoList.stream().forEach(e -> groupActionIdList.add(e.getId()));
        if (groupActionIdList.size() >0){
            groupActionMapper.deleteGroupActionProcessArea(groupActionIdList);
            groupActionMapper.deleteProjectGroupAction(groupUuid);
        }
        for (ProjectGroupActionVo actionVo : roleActionVoList){
            groupActionMapper.insertProjectGroupAction(actionVo);
            if (ModuleType.PROCESS.getValue().equals(actionVo.getModule())){
                groupActionMapper.insertProjectGroupActionProcess(actionVo.getId(), actionVo.getProcessAreaUuid());
            }
        }
    }

    @Override
    public List<ProjectGroupActionVo> searchProjectRoleAction(String groupUuid, String module) {
        return groupActionMapper.searchGroupActionByGroupUuidAndModule(groupUuid, module);
    }

    @ActionCheck(name = "检查", value = "check")
    @Override
    public void test(@InputParam ActionCheckVo actionCheckVo) {
        System.out.println("aa");
    }
}
