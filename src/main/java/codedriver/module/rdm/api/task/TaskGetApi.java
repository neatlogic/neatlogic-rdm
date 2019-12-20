package codedriver.module.rdm.api.task;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName TaskSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskGetApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/task/get";
    }

    @Override
    public String getName() {
        return "查询任务接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name="uuid", type= ApiParamType.STRING, isRequired = true)})
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        return null;
    }

}


