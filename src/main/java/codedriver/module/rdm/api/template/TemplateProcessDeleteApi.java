package codedriver.module.rdm.api.template;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.dto.TemplateProcessAreaVo;
import codedriver.module.rdm.services.ProjectTemplateService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-17 18:13
 **/
@Service
public class TemplateProcessDeleteApi extends ApiComponentBase {

    @Autowired
    private ProjectTemplateService templateService;

    @Override
    public String getToken() {
        return "module/rdm/projectTemplate/processAreaDelete";
    }

    @Override
    public String getName() {
        return "项目模板过程域删除接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "templateUuid", type = ApiParamType.STRING, desc = "模板uuid", isRequired = true),
            @Param(name = "id", type = ApiParamType.LONG, desc = "主键ID", isRequired = true)})
    @Description(desc = "项目模板过程域删除接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String templateUuid = jsonObj.getString("templateUuid");
        Long id = jsonObj.getLong("id");
        TemplateProcessAreaVo processAreaVo = new TemplateProcessAreaVo();
        processAreaVo.setProcessAreaUuid(processAreaUuid);
        processAreaVo.setTemplateUuid(templateUuid);
        processAreaVo.setId(id);
        templateService.deleteTemplateProcessArea(processAreaVo);
        return null;
    }
}
