package codedriver.module.rdm.api.project;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.constants.ProjectStatusType;
import codedriver.module.rdm.dto.ProjectVo;
import codedriver.module.rdm.services.ProjectService;
import codedriver.module.rdm.util.UuidUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.SchemaOutputResolver;

/**
 * @ClassName ProjectSaveApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectSaveApi extends ApiComponentBase {

    @Autowired
    private ProjectService projectService;

    @Override
    public String getToken() {
        return "module/rdm/project/save";
    }

    @Override
    public String getName() {
        return "保存项目接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name="name", type= ApiParamType.STRING, desc = "项目名称", isRequired = true, xss = true),
            @Param(name="description", type= ApiParamType.STRING, desc = "项目描述", isRequired = true, xss = true),
            @Param(name = "templateUuid", type = ApiParamType.STRING, desc = "模板uuid"),
            @Param(name="status", type= ApiParamType.STRING, desc = "状态"),
            @Param(name="uuid", type= ApiParamType.STRING, desc = "项目uuid")
    })

    @Output({
            @Param(name="uuid", type= ApiParamType.STRING),
    })
    @Description(desc = "保存项目接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject returnObj = new JSONObject();
        ProjectVo projectVo = new ProjectVo();
        projectVo.setName(jsonObj.getString("name"));
        projectVo.setDescription(jsonObj.getString("description"));
        projectVo.setUpdateUser(UserContext.get().getUserId());
        if (jsonObj.containsKey("uuid")){
            projectVo.setUuid(jsonObj.getString("uuid"));
        }
        if (jsonObj.containsKey("status")){
            projectVo.setStatus(jsonObj.getString("status"));
        }
        if (jsonObj.containsKey("templateUuid")){
            projectVo.setTemplateUuid(jsonObj.getString("templateUuid"));

        }
        returnObj.put("uuid", projectService.saveProject(projectVo));
        return returnObj;
    }
}
