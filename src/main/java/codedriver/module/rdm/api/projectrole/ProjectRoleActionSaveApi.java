package codedriver.module.rdm.api.projectrole;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
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
        return "module/rdm/projectroleaction/save";
    }

    @Override
    public String getName() {
        return "项目组权限添加接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "groupId", type = ApiParamType.LONG, desc = "组ID", isRequired = true),
            @Param(name = "actionIdList", type = ApiParamType.JSONARRAY, desc = "权限操作id集合", isRequired = true)})
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        Long groupId = jsonObj.getLong("groupId");
        JSONArray actionArray = jsonObj.getJSONArray("actionIdList");
        List<Long> actionList = new ArrayList<>();
        for (int i = 0; i < actionArray.size(); i++) {
            actionList.add(actionArray.getLong(i));
        }
        roleService.saveProjectRoleAction(groupId, actionList);
        return new JSONObject();
    }
}
