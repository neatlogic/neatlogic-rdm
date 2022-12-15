/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.task;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.TaskVo;
import codedriver.module.rdm.services.TaskService;

/**
 * @ClassName TaskSearchApi
 * @Description 根据uuid查询任务接口
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskGetApi extends PrivateApiComponentBase {

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


