package codedriver.module.rdm.api.projectworkflow;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName ProjectWorkflowSaveApi
 * @Description
 * @Auther r2d2
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectWorkflowSaveApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/projectworkflow/save";
    }

    @Override
    public String getName() {
        return "保存项目工作流接口";
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
