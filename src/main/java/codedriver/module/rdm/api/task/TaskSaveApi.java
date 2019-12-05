package codedriver.module.rdm.api.task;

import codedriver.framework.common.AuthAction;
import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName TaskSaveApi
 * @Description
 * @Auther r2d2
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskSaveApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/task/save";
    }

    @Override
    public String getName() {
        return "保存任务接口";
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


