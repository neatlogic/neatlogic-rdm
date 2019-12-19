package codedriver.module.rdm.api.template;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.TemplateProcessAreaTemplateVo;
import codedriver.module.rdm.services.ProjectTemplateService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-19 10:46
 **/
@Service
public class TemplateProcessTemplateSaveApi extends ApiComponentBase {

    @Autowired
    private ProjectTemplateService templateService;

    @Override
    public String getToken() {
        return "module/rdm/template/processTemplateSave";
    }

    @Override
    public String getName() {
        return "项目模板过程域模板内容保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "id", type = ApiParamType.LONG, desc = "主键ID"),
             @Param( name = "templateUuid", type = ApiParamType.STRING, desc = "模板uuid", isRequired = true),
             @Param( name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
             @Param( name = "content", type = ApiParamType.STRING, desc = "过程域模板内容")
    })
    @Description(desc = "项目模板过程域模板内容保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject returnObj = new JSONObject();
        TemplateProcessAreaTemplateVo processAreaTemplateVo = new TemplateProcessAreaTemplateVo();
        String templateUuid = jsonObj.getString("templateUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String content = jsonObj.getString("content");
        if (jsonObj.containsKey("id")){
            processAreaTemplateVo.setId(jsonObj.getLong("id"));
        }
        processAreaTemplateVo.setProcessAreaUuid(processAreaUuid);
        processAreaTemplateVo.setTemplateUuid(templateUuid);
        processAreaTemplateVo.setContent(content);
        templateService.saveTemplateProcessAreaTemplate(processAreaTemplateVo);
        returnObj.put("id", processAreaTemplateVo.getId());
        return returnObj;
    }
}
