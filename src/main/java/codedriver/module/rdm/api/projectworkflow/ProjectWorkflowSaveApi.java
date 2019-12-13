package codedriver.module.rdm.api.projectworkflow;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.ProjectStatusVo;
import codedriver.module.rdm.services.ProjectWorkflowService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProjectWorkflowSaveApi
 * @Description 保存项目工作流接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectWorkflowSaveApi extends ApiComponentBase {

    @Resource
    private ProjectWorkflowService projectWorkFlowService;

    @Override
    public String getToken() {
        return "module/rdm/projectworkflow/save";
    }

    @Override
    public String getName() {
        return "保存项目工作流接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "statusList", type = ApiParamType.JSONARRAY, desc = "工作流状态集合", isRequired = true)
    })
    @Output({})
    @Description(desc = "保存项目工作流接口")

    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        JSONArray statusArray = jsonObj.getJSONArray("statusList");
        List<ProjectStatusVo> statusList = new ArrayList<>();
        for(Object statusObject : statusArray){
            ProjectStatusVo projectStatusVo = new ProjectStatusVo();
            JSONObject statusJsonObject = JSONObject.parseObject(statusObject.toString());
            String statusName = statusJsonObject.getString("statusName");
            String statusUuid = statusJsonObject.getString("statusUuid");
            String statusType = statusJsonObject.getString("statusType");
            JSONArray transferArray  = statusJsonObject.getJSONArray("transferList");
            List<String> transferList = new ArrayList<>();
            for(Object transferObject : transferArray){
                transferList.add(transferObject.toString());
            }

            projectStatusVo.setProjectUuid(projectUuid);
            projectStatusVo.setProcessAreaUuid(processAreaUuid);
            projectStatusVo.setName(statusName);
            projectStatusVo.setUuid(statusUuid);
            projectStatusVo.setType(statusType);
            projectStatusVo.setTransferTo(transferList);
            statusList.add(projectStatusVo);
        }
        projectWorkFlowService.saveProjectWorkFlow(projectUuid, processAreaUuid, statusList);
        return null;
    }

}
