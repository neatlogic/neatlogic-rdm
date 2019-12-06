package codedriver.module.rdm.api.projectstatus;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName ProjectStatusSaveApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectStatusSaveApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/projectstatus/save";
    }

    @Override
    public String getName() {
        return "保存项目状态接口";
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
