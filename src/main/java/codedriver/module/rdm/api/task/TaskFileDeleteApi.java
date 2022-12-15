/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.TaskMapper;
import codedriver.module.rdm.dto.TaskFileVo;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-26 18:50
 **/
@Service
public class TaskFileDeleteApi extends PrivateApiComponentBase {

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

    @Input({@Param(name = "fileUuid", type = ApiParamType.STRING, desc = "文件Uuid", isRequired = true),
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
