package codedriver.module.rdm.api.template;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-18 15:25
 **/
@Service
public class TemplateProcessPrioritySaveApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/template/processPrioritySave";
    }

    @Override
    public String getName() {
        return "项目模板过程域优先级保存接口";
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
