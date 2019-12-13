package codedriver.module.rdm.api.projectworkflow;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dto.ProjectStatusVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProjectWorkflowGetApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectWorkflowGetApi extends ApiComponentBase {

    @Resource
    private ProjectWorkflowMapper projectWorkflowMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectworkflow/get";
    }

    @Override
    public String getName() {
        return "查询项目工作流接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
    })
    @Output({ @Param(name = "statusList", type = ApiParamType.JSONARRAY, desc = "项目状态集合", explode = ProjectStatusVo[].class)})
    @Description(desc = "查询项目工作流接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        result.put("statusList", projectWorkflowMapper.getProjectWorkFlow(projectUuid, processAreaUuid));
        return result;
    }


}
