package codedriver.module.rdm.api.projectrole;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMemberMapper;
import codedriver.module.rdm.dto.ProjectMemberVo;
import codedriver.module.rdm.dto.RoleActionVo;
import codedriver.module.rdm.services.ProjectRoleService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 14:39
 **/
@Service
public class ProjectRoleActionSearchApi extends ApiComponentBase {


    @Autowired
    private ProjectMemberMapper memberMapper;

    @Autowired
    private ProjectRoleService roleService;

    @Override
    public String getToken() {
        return "module/rdm/projectroleaction/search";
    }

    @Override
    public String getName() {
        return "获取用户权限接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "module", desc = "模块类型", type = ApiParamType.STRING, isRequired = true),
             @Param( name = "projectUuid", desc = "项目Uuid", type = ApiParamType.STRING, isRequired = true)})
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String module = jsonObj.getString("module");
        String projectUuid = jsonObj.getString("projectUuid");
        ProjectMemberVo memberVo = memberMapper.getProjectMember(projectUuid, UserContext.get().getUserId());
        if (memberVo != null){
            List<RoleActionVo> actionVoList = roleService.searchProjectRoleAction(memberVo.getGroupId(), module);
            List<String> actionNameList = new ArrayList<>();
            actionVoList.stream().forEach(e -> actionNameList.add(e.getAction()));
            JSONObject returnObj = new JSONObject();
            returnObj.put("actionList", actionNameList);
            return returnObj;
        }
        return new JSONObject();
    }
}
