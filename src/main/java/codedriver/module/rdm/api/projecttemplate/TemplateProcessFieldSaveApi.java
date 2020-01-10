package codedriver.module.rdm.api.projecttemplate;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.services.ProjectTemplateService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-18 15:25
 **/
@Service
public class TemplateProcessFieldSaveApi extends ApiComponentBase {

    @Autowired
    private ProjectTemplateService templateService;

    @Override
    public String getToken() {
        return "module/rdm/projecttemplate/processfield/save";
    }

    @Override
    public String getName() {
        return "项目模板过程域属性值保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "fieldUuid", type = ApiParamType.STRING, desc = "属性uuid", isRequired = true),
            @Param(name = "config", type = ApiParamType.JSONOBJECT, desc = "属性配置", isRequired = true)})
    @Description(desc = "项目模板过程域属性值保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String fieldUuid = jsonObj.getString("fieldUuid");
        JSONObject configObj = jsonObj.getJSONObject("config");
        TemplateProcessAreaFieldVo fieldVo = new TemplateProcessAreaFieldVo();
        fieldVo.setFieldUuid(fieldUuid);
        fieldVo.setConfig(configObj.toJSONString());
        templateService.saveTemplateProcessAreaFieldConfig(fieldVo);
        return new JSONObject();
    }
}
