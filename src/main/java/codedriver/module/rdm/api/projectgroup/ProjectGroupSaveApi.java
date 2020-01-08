package codedriver.module.rdm.api.projectgroup;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.ProjectGroupVo;
import codedriver.module.rdm.services.ProjectGroupService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 15:13
 **/
@Service
public class ProjectGroupSaveApi extends ApiComponentBase {

    @Autowired
    private ProjectGroupService groupService;

    @Override
    public String getToken() {
        return "module/rdm/projectgroup/save";
    }

    @Override
    public String getName() {
        return "项目组保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "id", type = ApiParamType.LONG, desc = "主键ID"),
             @Param( name = "name", type = ApiParamType.STRING, desc = "项目组名称", isRequired = true),
             @Param( name = "projectUuid", type = ApiParamType.STRING, desc = "项目Uuid", isRequired = true),
             @Param( name = "role", type = ApiParamType.STRING, isRequired = true)})
    @Output( @Param( name = "id", type = ApiParamType.LONG, desc = "主键ID"))
    @Description(desc = "项目组保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        ProjectGroupVo groupVo = new ProjectGroupVo();
        groupVo.setId(jsonObj.getLong("id"));
        groupVo.setName(jsonObj.getString("name"));
        groupVo.setProjectUuid(jsonObj.getString("projectUuid"));
        groupVo.setRole(jsonObj.getString("role"));
        groupService.saveProjectGroup(groupVo);
        JSONObject returnObj = new JSONObject();
        returnObj.put("id", groupVo.getId());
        return returnObj;
    }
}
