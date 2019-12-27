package codedriver.module.rdm.api.projectiteration;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.services.ProjectIterationService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectIterationSaveApi
 * @Description 项目迭代规划接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectIterationAssociateApi extends ApiComponentBase {

    @Resource
    private ProjectIterationService projectIterationService;

    @Override
    public String getToken() {
        return "module/rdm/projectiteration/associate";
    }

    @Override
    public String getName() {
        return "项目迭代规划接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "projectIterationUuid", type = ApiParamType.STRING, desc = "项目迭代Uuid", isRequired = true),
            @Param(name = "taskList", type = ApiParamType.STRING, desc = "Task uuid集合", isRequired = true),
            @Param(name = "action", type = ApiParamType.STRING, desc = "操作类型 add 或 delete", isRequired = true)
    })
    @Description(desc = "项目迭代规划接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        String projectIterationUuid = jsonObj.getString("projectIterationUuid");
        String action = jsonObj.getString("action");
        JSONArray taskArray = jsonObj.getJSONArray("taskList");
        List<String> taskList = taskArray.toJavaList(String.class);
        if (taskArray != null && taskArray.size() > 0) {
            projectIterationService.associateTask(projectUuid, projectIterationUuid, action, taskList);
        }

        return null;
    }


}
