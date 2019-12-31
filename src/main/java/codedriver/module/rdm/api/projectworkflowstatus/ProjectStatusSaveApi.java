package codedriver.module.rdm.api.projectworkflowstatus;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.ProjectWorkFlowStatusVo;
import codedriver.module.rdm.services.ProjectWorkflowService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProjectStatusSaveApi
 * @Description 保存项目状态接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectStatusSaveApi extends ApiComponentBase {

    @Resource
    private ProjectWorkflowService projectWorkflowService;

    @Override
    public String getToken() {
        return "module/rdm/projectstatus/save";
    }

    @Override
    public String getName() {
        return "保存项目状态接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "name", type = ApiParamType.STRING, desc = "名称", isRequired = true),
            @Param(name = "type", type = ApiParamType.STRING, desc = "状态类型", isRequired = true),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "状态uuid", isRequired = false)
    })
    @Output({@Param(name="projectStatusVo", type = ApiParamType.JSONOBJECT, desc = "状态信息", explode = ProjectWorkFlowStatusVo.class)})
    @Description(desc = "保存项目状态接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {

        ProjectWorkFlowStatusVo projectWorkFlowStatusVo = new ProjectWorkFlowStatusVo();

        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String name = jsonObj.getString("name");
        String type = jsonObj.getString("type");

        projectWorkFlowStatusVo.setProjectUuid(projectUuid);
        projectWorkFlowStatusVo.setProcessAreaUuid(processAreaUuid);
        projectWorkFlowStatusVo.setName(name);
        projectWorkFlowStatusVo.setType(type);

        if(jsonObj.containsKey("uuid") && StringUtils.isNotBlank(jsonObj.getString("uuid"))){
            String uuid = jsonObj.getString("uuid");
            projectWorkFlowStatusVo.setUuid(uuid);
            projectWorkFlowStatusVo.setUpdateUser(UserContext.get().getUserId());
        }else{
            projectWorkFlowStatusVo.setCreateUser(UserContext.get().getUserId());
        }

        String uuid = projectWorkflowService.saveProjectStatus(projectWorkFlowStatusVo);
        projectWorkFlowStatusVo.setUuid(uuid);
        return projectWorkFlowStatusVo;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.arch.data.model"));
    }

}