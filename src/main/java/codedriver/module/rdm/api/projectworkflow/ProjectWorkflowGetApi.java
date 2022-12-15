/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectworkflow;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dto.ProjectWorkFlowStatusVo;

/**
 * @ClassName ProjectWorkflowGetApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectWorkflowGetApi extends PrivateApiComponentBase {

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
    @Output({@Param(name = "statusList", type = ApiParamType.JSONARRAY, desc = "项目状态集合", explode = ProjectWorkFlowStatusVo[].class)})
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
