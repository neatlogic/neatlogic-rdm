package codedriver.module.rdm.api.task;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName TaskDeleteApi
 * @Description
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskDeleteApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/task/delete";
    }

    @Override
    public String getName() {
        return "删除任务接口";
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


