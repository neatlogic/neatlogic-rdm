package codedriver.module.rdm.api.projectstatus;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName ProjectStatusSearchApi
 * @Description
 * @Auther r2d2
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectStatusSearchApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/projectstatus/search";
    }

    @Override
    public String getName() {
        return "查询项目状态接口";
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
