package codedriver.module.rdm.api;

import codedriver.framework.common.AuthAction;
import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName TaskCreateApi
 * @Description
 * @Auther fandong
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskCreateApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/task/create";
    }

    @Override
    public String getName() {
        return "创建任务接口";
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


