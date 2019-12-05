package codedriver.module.rdm.api.task;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName TaskSearchApi
 * @Description
 * @Auther r2d2
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskSearchApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/task/search";
    }

    @Override
    public String getName() {
        return "查询任务接口";
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


