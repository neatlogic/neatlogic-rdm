package codedriver.module.rdm.api.taskassociate;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.services.TaskService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName TaskAssociateDeleteApi
 * @Description 删除任务关联接口
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskAssociateDeleteApi extends ApiComponentBase {

    @Resource
    private TaskService taskService;

    @Override
    public String getToken() {
        return "module/rdm/task/associate/delete";
    }

    @Override
    public String getName() {
        return "删除任务关联接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "taskUuid", type = ApiParamType.STRING, isRequired = true),
            @Param(name = "targetUuid", type = ApiParamType.STRING, isRequired = true)
    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String taskUuid = jsonObj.getString("taskUuid");
        String targetUuid = jsonObj.getString("targetUuid");
        taskService.deleteAssociate(taskUuid, targetUuid);
        return null;
    }

}


