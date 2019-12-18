package codedriver.module.rdm.api.template;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.services.ProjectTemplateService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-18 15:01
 **/
@Service
public class TemplateDeleteApi extends ApiComponentBase {

    @Autowired
    private ProjectTemplateService projectTemplateService;

    @Override
    public String getToken() {
        return "module/rdm/projectTemplate/delete";
    }

    @Override
    public String getName() {
        return "项目模板删除接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param(name = "uuid", type = ApiParamType.STRING, desc = "模板uuid", isRequired = true)})
    @Description(desc = "项目模板删除接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String uuid = jsonObj.getString("uuid");
        projectTemplateService.deleteTemplate(uuid);
        return new JSONObject();
    }
}
