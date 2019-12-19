package codedriver.module.rdm.api.task;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName TaskSaveApi
 * @Description
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskSaveApi extends ApiComponentBase {

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
            @Param(name = "categoryUuid", type = ApiParamType.STRING, desc = "类别uuid", isRequired = false),

            @Param(name = "startTime", type = ApiParamType.STRING, desc = "开始时间", isRequired = false),
            @Param(name = "endTime", type = ApiParamType.STRING, desc = "截至时间", isRequired = false),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "父任务id", isRequired = false),
            @Param(name = "processAccountIdList", type = ApiParamType.JSONARRAY, desc = "处理人集合", isRequired = false),
            @Param(name = "customFieldList", type = ApiParamType.JSONARRAY, desc = "项目自定义属性集合", isRequired = false ),
            @Param(name = "description", type = ApiParamType.STRING, desc = "描述", isRequired = false),
    })
    @Output({@Param(name = "uuid", type = ApiParamType.STRING, desc = "任务uuid")})
    @Description(desc = "保存任务接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String uuid = null;

        return uuid;
    }

}


