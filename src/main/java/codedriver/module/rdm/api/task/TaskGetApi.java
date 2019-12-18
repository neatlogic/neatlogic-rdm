package codedriver.module.rdm.api.task;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName TaskGetApi
 * @Description 根据uuid获取任务详情接口
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
        return "根据uuid获取任务详情接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name="uuid", type= ApiParamType.STRING, desc = "任务uuid", isRequired = true)})
    @Description(desc = "根据uuid获取任务详情接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        return null;
    }

}


