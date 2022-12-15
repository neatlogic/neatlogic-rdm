/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectworkflow;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.ProjectWorkFlowStatusVo;
import codedriver.module.rdm.services.ProjectWorkflowService;

/**
 * @ClassName ProjectWorkflowSaveApi
 * @Description 保存项目工作流接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectWorkflowSaveApi extends PrivateApiComponentBase {

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
        List<ProjectWorkFlowStatusVo> statusList = new ArrayList<>();
        for (Object statusObject : statusArray) {
            ProjectWorkFlowStatusVo projectWorkFlowStatusVo = new ProjectWorkFlowStatusVo();
            JSONObject statusJsonObject = JSONObject.parseObject(statusObject.toString());
            String statusName = statusJsonObject.getString("statusName");
            String statusUuid = statusJsonObject.getString("statusUuid");
            String statusType = statusJsonObject.getString("statusType");
            JSONArray transferArray = statusJsonObject.getJSONArray("transferList");
            List<String> transferList = new ArrayList<>();
            for (Object transferObject : transferArray) {
                transferList.add(transferObject.toString());
            }

            projectWorkFlowStatusVo.setProjectUuid(projectUuid);
            projectWorkFlowStatusVo.setProcessAreaUuid(processAreaUuid);
            projectWorkFlowStatusVo.setName(statusName);
            projectWorkFlowStatusVo.setUuid(statusUuid);
            projectWorkFlowStatusVo.setType(statusType);
            projectWorkFlowStatusVo.setTransferTo(transferList);
            statusList.add(projectWorkFlowStatusVo);
        }
        projectWorkFlowService.saveProjectWorkFlow(projectUuid, processAreaUuid, statusList);
        return null;
    }

}
