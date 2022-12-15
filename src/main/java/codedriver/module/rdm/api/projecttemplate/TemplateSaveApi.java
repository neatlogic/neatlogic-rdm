/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projecttemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.TemplateVo;
import codedriver.module.rdm.services.ProjectTemplateService;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-16 16:52
 **/
@Service
public class TemplateSaveApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectTemplateService templateService;

    @Override
    public String getToken() {
        return "module/rdm/projecttemplate/save";
    }

    @Override
    public String getName() {
        return "项目模板保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "name", type = ApiParamType.STRING, isRequired = true, desc = "模板名称", xss = true),
            @Param(name = "description", type = ApiParamType.STRING, isRequired = true, desc = "模板描述", xss = true),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "模板UUID")})
    @Output({@Param(name = "uuid", type = ApiParamType.STRING, desc = "模板UUID")})
    @Description(desc = "项目模板保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject returnObj = new JSONObject();
        TemplateVo templateVo = new TemplateVo();
        templateVo.setName(jsonObj.getString("name"));
        templateVo.setDescription(jsonObj.getString("description"));
        if (jsonObj.containsKey("uuid")) {
            templateVo.setUuid(jsonObj.getString("uuid"));
        }
        returnObj.put("uuid", templateService.saveTemplate(templateVo));
        return returnObj;
    }
}
