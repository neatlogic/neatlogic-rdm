package codedriver.module.rdm.api.task;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.TaskVo;
import com.alibaba.fastjson.JSONObject;
import javafx.concurrent.Task;
import org.springframework.stereotype.Service;

/**
 * @ClassName TaskSearchApi
 * @Description
 * @Auther
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

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
    })

    @Output({
            @Param(name = "taskList", type = ApiParamType.JSONARRAY, desc = "任务集合", explode = TaskVo[].class)
    })
    @Description(desc = "查询任务接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        return null;
    }

}


