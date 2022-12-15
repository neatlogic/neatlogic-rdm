/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectworkflowstatus;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.ProjectWorkFlowStatusVo;
import codedriver.module.rdm.services.ProjectWorkflowService;

/**
 * @ClassName ProjectStatusSaveApi
 * @Description 保存项目状态接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectStatusSaveApi extends PrivateApiComponentBase {

    @Resource
    private ProjectWorkflowService projectWorkflowService;

    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.arch.data.model"));
    }

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
    @Output({@Param(name = "projectStatusVo", type = ApiParamType.JSONOBJECT, desc = "状态信息", explode = ProjectWorkFlowStatusVo.class)})
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

        if (jsonObj.containsKey("uuid") && StringUtils.isNotBlank(jsonObj.getString("uuid"))) {
            String uuid = jsonObj.getString("uuid");
            projectWorkFlowStatusVo.setUuid(uuid);
            projectWorkFlowStatusVo.setUpdateUser(UserContext.get().getUserId());
        } else {
            projectWorkFlowStatusVo.setCreateUser(UserContext.get().getUserId());
        }

        String uuid = projectWorkflowService.saveProjectStatus(projectWorkFlowStatusVo);
        projectWorkFlowStatusVo.setUuid(uuid);
        return projectWorkFlowStatusVo;
    }

}
