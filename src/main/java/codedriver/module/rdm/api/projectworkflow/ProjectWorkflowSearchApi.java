package codedriver.module.rdm.api.projectworkflow;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName ProjectWorkflowSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectWorkflowSearchApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/projectworkflow/search";
    }

    @Override
    public String getName() {
        return "查询项目工作流接口";
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
