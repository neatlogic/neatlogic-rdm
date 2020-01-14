package codedriver.module.rdm.api.projectrole;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.framework.restful.annotation.Input;
import codedriver.module.rdm.dto.RoleActionVo;
import codedriver.module.rdm.services.ProjectRoleService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 11:01
 **/
@Service
public class ProjectRoleActionSaveApi extends ApiComponentBase {


    @Autowired
    private ProjectRoleService roleService;

    @Override
    public String getToken() {
        return "module/rdm/projectgroupaction/save";
    }

    @Override
    public String getName() {
        return "项目组权限添加接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "groupUuid", type = ApiParamType.STRING, desc = "组UUID", isRequired = true),
             @Param( name = "moduleList", type = ApiParamType.JSONARRAY, desc = "模块权限操作集合", isRequired = true)})
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String groupUuid = jsonObj.getString("groupUuid");
        JSONArray actionArray = jsonObj.getJSONArray("moduleList");
        List<RoleActionVo> roleActionVoList = new ArrayList<>();
        for (int i = 0 ; i < actionArray.size(); i++){
            JSONObject obj = actionArray.getJSONObject(i);
            String module = obj.getString("module");
            JSONArray actionList = obj.getJSONArray("actionList");
            for (int j = 0; j < actionList.size(); j++){
                RoleActionVo actionVo = new RoleActionVo();
                actionVo.setAction(actionList.getString(j));
                actionVo.setGroupUuid(groupUuid);
                actionVo.setCreateUser(UserContext.get().getUserId());
                actionVo.setModule(module);
                roleActionVoList.add(actionVo);
            }
        }
        roleService.saveProjectRoleAction(groupUuid, roleActionVoList);
        return new JSONObject();
    }
}
