package codedriver.module.rdm.api.template;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-18 15:26
 **/
@Service
public class TemplateProcessCirculateSaveApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/template/processCirculateSave";
    }

    @Override
    public String getName() {
        return "项目模板过程域流转状态保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        return null;
    }
}
