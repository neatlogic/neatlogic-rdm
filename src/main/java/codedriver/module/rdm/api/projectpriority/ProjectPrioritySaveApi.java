/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectpriority;

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
import codedriver.module.rdm.dto.ProjectPriorityVo;
import codedriver.module.rdm.services.ProjectPriorityService;

/**
 * @ClassName ProjectStatusSaveApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectPrioritySaveApi extends PrivateApiComponentBase {

    @Resource
    private ProjectPriorityService projectPriorityService;

    @Override
    public String getToken() {
        return "module/rdm/projectpriority/save";
    }

    @Override
    public String getName() {
        return "保存项目优先级接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "name", type = ApiParamType.STRING, desc = "名称", isRequired = true),
            @Param(name = "color", type = ApiParamType.STRING, desc = "颜色", isRequired = false),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "状态uuid", isRequired = false)
    })
    @Output({@Param(name = "projectPriorityVo", type = ApiParamType.JSONOBJECT, desc = "projectPriorityVo", explode = ProjectPriorityVo.class)})
    @Description(desc = "保存项目优先级接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {

        JSONObject result = new JSONObject();
        ProjectPriorityVo projectPriorityVo = new ProjectPriorityVo();

        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String name = jsonObj.getString("name");
        String color = jsonObj.getString("color");

        projectPriorityVo.setProjectUuid(projectUuid);
        projectPriorityVo.setProcessAreaUuid(processAreaUuid);
        projectPriorityVo.setName(name);
        projectPriorityVo.setColor(color);


        if (jsonObj.containsKey("uuid") && StringUtils.isNotBlank(jsonObj.getString("uuid"))) {
            String uuid = jsonObj.getString("uuid");
            projectPriorityVo.setUuid(uuid);
            projectPriorityVo.setUpdateUser(UserContext.get().getUserId());
        } else {
            projectPriorityVo.setCreateUser(UserContext.get().getUserId());
        }

        String uuid = projectPriorityService.saveProjectPriority(projectPriorityVo);
        result.put("uuid", uuid);
        return result;
    }


}
