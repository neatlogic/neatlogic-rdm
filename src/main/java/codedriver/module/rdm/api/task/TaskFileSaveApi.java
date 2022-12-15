/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.TaskFileVo;
import codedriver.module.rdm.dto.TaskVo;
import codedriver.module.rdm.services.TaskService;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-26 10:47
 **/
@Service
public class TaskFileSaveApi extends PrivateApiComponentBase {

    @Autowired
    private TaskService taskService;

    @Override
    public String getToken() {
        return "module/rdm/taskfile/save";
    }

    @Override
    public String getName() {
        return "任务附件保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "taskUuid", type = ApiParamType.STRING, desc = "任务uuid", isRequired = true),
            @Param(name = "fileUuidList", type = ApiParamType.JSONARRAY, desc = "文件uuid集合", isRequired = true)})
    @Output({@Param(name = "idArray", type = ApiParamType.JSONARRAY, desc = "id集合")})
    @Description(desc = "任务附件保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String taskUuid = jsonObj.getString("taskUuid");
        TaskVo taskVo = new TaskVo();
        taskVo.setUuid(taskUuid);
        JSONArray fileUuidArray = jsonObj.getJSONArray("fileUuidList");
        if (fileUuidArray.size() > 0) {
            List<TaskFileVo> taskFileVoList = new ArrayList<>();
            for (int i = 0; i < fileUuidArray.size(); i++) {
                String fileUuid = fileUuidArray.getString(i);
                TaskFileVo fileVo = new TaskFileVo();
                fileVo.setTaskUuid(taskUuid);
                fileVo.setFileUuid(fileUuid);
                fileVo.setCreateUser(UserContext.get().getUserId());
                taskFileVoList.add(fileVo);
            }
            taskVo.setTaskFileVoList(taskFileVoList);
        }
        JSONObject returnObj = new JSONObject();
        returnObj.put("idList", taskService.saveTaskFile(taskVo));
        return returnObj;
    }
}
