package codedriver.module.rdm.api.task;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.FieldVo;
import codedriver.module.rdm.dto.TaskFieldVo;
import codedriver.module.rdm.dto.TaskFileVo;
import codedriver.module.rdm.dto.TaskVo;
import codedriver.module.rdm.services.TaskService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TaskSaveApi
 * @Description
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskSaveApi extends ApiComponentBase {

    @Resource
    private TaskService taskService;

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

    @Input({@Param(name = "name", type = ApiParamType.STRING, desc = "名称", isRequired = true),
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),

            @Param(name = "statusUuid", type = ApiParamType.STRING, desc = "状态uuid", isRequired = false),
            @Param(name = "priorityUuid", type = ApiParamType.STRING, desc = "优先级uuid", isRequired = false),
            @Param(name = "iterationUuid", type = ApiParamType.STRING, desc = "迭代uuid", isRequired = false),
            @Param(name = "iterationName", type = ApiParamType.STRING, desc = "迭代名称", isRequired = false),

            @Param(name = "categoryUuid", type = ApiParamType.STRING, desc = "类别uuid", isRequired = false),

            @Param(name = "startTime", type = ApiParamType.STRING, desc = "开始时间", isRequired = false),
            @Param(name = "endTime", type = ApiParamType.STRING, desc = "截至时间", isRequired = false),
            @Param(name = "parentUuid", type = ApiParamType.STRING, desc = "父任务Uuid", isRequired = false),
            @Param(name = "processAccountIdList", type = ApiParamType.JSONARRAY, desc = "处理人集合", isRequired = false),
            @Param(name = "customFieldList", type = ApiParamType.JSONARRAY, desc = "项目自定义属性集合", isRequired = false),
            @Param(name = "attachmentList", type = ApiParamType.JSONARRAY, desc = "附件集合", isRequired = false),
            @Param(name = "associateList", type = ApiParamType.JSONARRAY, desc = "关联Task集合", isRequired = false),
            @Param(name = "description", type = ApiParamType.STRING, desc = "描述", isRequired = false),
    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        TaskVo taskVo = new TaskVo();

        String name = jsonObj.getString("name");
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String priorityUuid = jsonObj.getString("priorityUuid");
        String statusUuid = jsonObj.getString("statusUuid");
        String iterationUuid = jsonObj.getString("iterationUuid");
        String iterationName = jsonObj.getString("iterationName");

        String categoryUuid = jsonObj.getString("categoryUuid");
        String parentUuid = jsonObj.getString("parentUuid");
        String startTime = jsonObj.getString("startTime");
        String endTime = jsonObj.getString("endTime");

        String description = jsonObj.getString("description");
        JSONArray processAccountIdArray = jsonObj.getJSONArray("processAccountIdList");
        JSONArray fieldArray = jsonObj.getJSONArray("customFieldList");
        JSONArray fileUuidArray = jsonObj.getJSONArray("attachmentList");
        if (fileUuidArray != null && fileUuidArray.size() > 0){
            List<TaskFileVo> taskFileVoList = new ArrayList<>();
            for (int i = 0; i < fileUuidArray.size(); i++){
                String fileUuid = fileUuidArray.getString(i);
                TaskFileVo fileVo = new TaskFileVo();
                fileVo.setFileUuid(fileUuid);
                fileVo.setCreateUser(UserContext.get().getUserId());
                taskFileVoList.add(fileVo);
            }
            taskVo.setTaskFileVoList(taskFileVoList);
        }
        taskVo.setName(name);
        taskVo.setProjectUuid(projectUuid);
        taskVo.setProcessAreaUuid(processAreaUuid);
        taskVo.setPriorityUuid(priorityUuid);
        taskVo.setIterationUuid(iterationUuid);
        taskVo.setStatusUuid(statusUuid);
        taskVo.setIterationName(iterationName);
        taskVo.setCategoryUuid(categoryUuid);
        taskVo.setParentUuid(parentUuid);
        taskVo.setStartTime(startTime);
        taskVo.setEndTime(endTime);
        taskVo.setDescription(description);

        if(processAccountIdArray != null && processAccountIdArray.size() > 0 ){
            List<String> accountIdList = new ArrayList<>();
            for(Object account : processAccountIdArray){
                accountIdList.add(account.toString());
            }
            taskVo.setProcessAccountIdList(accountIdList);
        }

        if(fieldArray != null && fieldArray.size() > 0 ){
            List<FieldVo> fieldList = new ArrayList<>();
            for(Object field : fieldArray){
               JSONObject fieldObj = JSONObject.parseObject(field.toString());
               FieldVo fieldVo = new FieldVo();

               String tField = fieldObj.getString("field");
               String tName = fieldObj.getString("name");
               String tUuid = fieldObj.getString("uuid");
               String tType = fieldObj.getString("type");
               String tValue = fieldObj.getString("value");
               fieldVo.setUuid(tUuid);
               fieldVo.setName(tName);
               fieldVo.setField(tField);
               fieldVo.setType(tType);
               fieldVo.setValue(tValue);
               fieldList.add(fieldVo);
            }
            taskVo.setTaskFieldList(fieldList);
        }
        String uuid = taskService.saveTask(taskVo);
        result.put("uuid",uuid);
        return result;
    }

}


