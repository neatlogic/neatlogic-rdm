package codedriver.module.rdm.api.projectworkflowstatus;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dto.ProjectWorkFlowStatusVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProcessStatusGetApi
 * @Description 根据uuid查询状态接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProcessStatusGetApi extends ApiComponentBase {

    @Resource
    private ProjectWorkflowMapper projectWorkflowMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectstatus/get";
    }

    @Override
    public String getName() {
        return "根据uuid查询状态接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "优先级uuid", isRequired = true)
    })

    @Output({
            @Param(name = "projectWorkFlowStatusVo", type = ApiParamType.JSONOBJECT, desc = "状态属性", explode = ProjectWorkFlowStatusVo.class)
    })
    @Description(desc = "根据uuid查询状态接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String uuid = jsonObj.getString("uuid");
        ProjectWorkFlowStatusVo ProjectWorkFlowStatusVo = projectWorkflowMapper.getProjectWorkflowStatus(projectUuid, processAreaUuid, uuid);
        return ProjectWorkFlowStatusVo;
    }


}
