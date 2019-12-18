package codedriver.module.rdm.api.template;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.framework.restful.annotation.Input;
import codedriver.module.rdm.dto.TemplateVo;
import codedriver.module.rdm.services.ProjectTemplateService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-16 16:52
 **/
@Service
public class TemplateSaveApi extends ApiComponentBase {

    @Autowired
    private ProjectTemplateService templateService;

    @Override
    public String getToken() {
        return "module/rdm/template/save";
    }

    @Override
    public String getName() {
        return "项目模板保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param(name = "name", type = ApiParamType.STRING, isRequired = true, desc = "模板名称", xss = true),
            @Param(name = "description", type = ApiParamType.STRING, isRequired = true, desc = "模板描述", xss = true),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "模板UUID")})
    @Output({ @Param(name = "uuid", type = ApiParamType.STRING, desc = "模板UUID")})
    @Description(desc = "项目模板保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject returnObj = new JSONObject();
        TemplateVo templateVo = new TemplateVo();
        templateVo.setName(jsonObj.getString("name"));
        templateVo.setDescription(jsonObj.getString("description"));
        if (jsonObj.containsKey("uuid")){
            templateVo.setUuid(jsonObj.getString("uuid"));
        }
        returnObj.put("uuid", templateService.saveTemplate(templateVo));
        return new JSONObject();
    }
}
