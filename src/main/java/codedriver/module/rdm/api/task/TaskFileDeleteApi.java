package codedriver.module.rdm.api.task;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.framework.restful.annotation.Input;
import codedriver.module.rdm.dao.mapper.TaskMapper;
import codedriver.module.rdm.dto.TaskFileVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-26 18:50
 **/
@Service
public class TaskFileDeleteApi extends ApiComponentBase {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public String getToken() {
        return "module/rdm/taskfile/delete";
    }

    @Override
    public String getName() {
        return "任务附件删除接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param(name = "fileUuid", type = ApiParamType.STRING, desc = "文件Uuid", isRequired = true),
             @Param(name = "taskUuid", type = ApiParamType.STRING, desc = "任务Uuid", isRequired = true)})
    @Description(desc = "任务附件删除接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        TaskFileVo fileVo = new TaskFileVo();
        fileVo.setTaskUuid(jsonObj.getString("taskUuid"));
        fileVo.setFileUuid(jsonObj.getString("fileUuid"));
        taskMapper.deleteTaskFile(fileVo);
        return new JSONObject();
    }
}
