package codedriver.module.rdm.api.task;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.TaskVo;
import codedriver.module.rdm.services.TaskService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName TaskSearchApi
 * @Description 根据uuid查询任务接口
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskGetApi extends ApiComponentBase {

    @Resource
    private TaskService taskService;

    @Override
    public String getToken() {
        return "module/rdm/task/get";
    }

    @Override
    public String getName() {
        return "根据uuid查询任务接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "uuid", type = ApiParamType.STRING, isRequired = true)
    })
    @Output({
            @Param(name = "taskVo", type = ApiParamType.JSONOBJECT, explode = TaskVo.class)
    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String uuid = jsonObj.getString("uuid");
        TaskVo taskVo = taskService.getTaskInfoByUuid(uuid);
        return taskVo;
    }

}


