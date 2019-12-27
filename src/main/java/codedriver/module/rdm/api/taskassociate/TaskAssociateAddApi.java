package codedriver.module.rdm.api.taskassociate;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.TaskAssociateVo;
import codedriver.module.rdm.services.TaskService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TaskAssociateAddApi
 * @Description 任务关联接口
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskAssociateAddApi extends ApiComponentBase {

    @Resource
    private TaskService taskService;

    @Override
    public String getToken() {
        return "module/rdm/task/associate/add";
    }

    @Override
    public String getName() {
        return "任务关联接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "taskUuid", type = ApiParamType.STRING, isRequired = true),
            @Param(name = "targetUuidList", type = ApiParamType.JSONARRAY, isRequired = true),
            @Param(name = "type", type = ApiParamType.STRING, isRequired = true)
    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String taskUuid = jsonObj.getString("taskUuid");
        String type = jsonObj.getString("type");
        JSONArray targetList = jsonObj.getJSONArray("targetUuidList");

        List<TaskAssociateVo> associateList = new ArrayList<>();
        for(Object target : targetList){
            TaskAssociateVo taskAssociateVo = new TaskAssociateVo(taskUuid, target.toString(), type);
            associateList.add(taskAssociateVo);
        }
        taskService.associateTask(associateList);
        return null;
    }

}


