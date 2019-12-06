package codedriver.module.rdm.api.processarea;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName ProcessAreaSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProcessAreaSearchApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/processarea/search";
    }

    @Override
    public String getName() {
        return "查询过程域接口";
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
