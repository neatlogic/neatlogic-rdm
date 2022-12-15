/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectprocessarea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.ProjectProcessAreaTemplateVo;
import codedriver.module.rdm.services.ProjectService;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-23 17:09
 **/
@Service
public class ProjectProcessTemplateSaveApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectService projectService;

    @Override
    public String getToken() {
        return "module/rdm/projectprocesstemplate/save";
    }

    @Override
    public String getName() {
        return "项目过程域模板保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "主键ID"),
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域Uuid", isRequired = true),
            @Param(name = "content", type = ApiParamType.STRING, desc = "过程域模板内容", isRequired = true)})
    @Description(desc = "项目过程域模板保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject returnObj = new JSONObject();
        ProjectProcessAreaTemplateVo templateVo = new ProjectProcessAreaTemplateVo();
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String content = jsonObj.getString("content");
        templateVo.setProjectUuid(projectUuid);
        templateVo.setProcessAreaUuid(processAreaUuid);
        templateVo.setContent(content);
        if (jsonObj.containsKey("id")) {
            templateVo.setId(jsonObj.getLong("id"));
        }
        projectService.saveProjectProcessAreaTemplate(templateVo);
        returnObj.put("id", templateVo.getId());
        return returnObj;
    }
}
