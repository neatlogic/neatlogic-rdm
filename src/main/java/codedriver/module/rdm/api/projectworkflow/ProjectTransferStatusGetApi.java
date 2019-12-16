package codedriver.module.rdm.api.projectworkflow;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProjectTransferStatusGetApi
 * @Description 查询当前状态的可流转状态接口
 * @Auther
 * @Date 2019/12/16 12:05
 **/
@Service
public class ProjectTransferStatusGetApi extends ApiComponentBase {

    @Resource
    private ProjectWorkflowMapper projectWorkflowMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectworkflow/transferstatus/get";
    }

    @Override
    public String getName() {
        return "查询当前状态的可流转状态接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "statusUuid", type = ApiParamType.STRING, desc = "状态uuid", isRequired = true)
    })

    @Description(desc = "查询当前状态的可流转状态接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String statusUuid = jsonObj.getString("statusUuid");
        result.put("statusList", projectWorkflowMapper.getTransferStatusList(projectUuid,processAreaUuid,statusUuid ));
        return result;
    }
}
