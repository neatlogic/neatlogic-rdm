package codedriver.module.rdm.api.projectiteration;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.ProjectIterationVo;
import codedriver.module.rdm.dto.ProjectPriorityVo;
import codedriver.module.rdm.services.ProjectIterationService;
import codedriver.module.rdm.services.ProjectPriorityService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProjectIterationSaveApi
 * @Description 保存项目迭代接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectIterationSaveApi extends ApiComponentBase {

    @Resource
    private ProjectIterationService projectIterationService;

    @Override
    public String getToken() {
        return "module/rdm/projectiteration/save";
    }

    @Override
    public String getName() {
        return "保存项目迭代接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "startDate", type = ApiParamType.STRING, desc = "开始日期，eg:1990-11-12", isRequired = true),
            @Param(name = "endDate", type = ApiParamType.STRING, desc = "结束日期，eg:1990-12-12", isRequired = true),
            @Param(name = "name", type = ApiParamType.STRING, desc = "名称", isRequired = true),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "迭代uuid", isRequired = false)
    })
    @Output({@Param(name = "uuid", type = ApiParamType.STRING, desc = "状态uuid")})
    @Description(desc="保存项目迭代接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        ProjectIterationVo projectIterationVo = new ProjectIterationVo();
        String projectUuid = jsonObj.getString("projectUuid");
        String name = jsonObj.getString("name");
        String description = jsonObj.getString("description");
        String startDate = jsonObj.getString("startDate");
        String endDate = jsonObj.getString("endDate");


        projectIterationVo.setProjectUuid(projectUuid);
        projectIterationVo.setName(name);
        projectIterationVo.setDescription(description);
        projectIterationVo.setStartDate(startDate);
        projectIterationVo.setEndDate(endDate);

        if(jsonObj.containsKey("uuid") && StringUtils.isNotBlank(jsonObj.getString("uuid"))){
            String uuid = jsonObj.getString("uuid");
            projectIterationVo.setUuid(uuid);
            projectIterationVo.setUpdateUser(UserContext.get().getUserId());
        }else{
            projectIterationVo.setCreateUser(UserContext.get().getUserId());
        }

        String uuid = projectIterationService.saveProjectIteration(projectIterationVo);
        result.put("uuid", uuid);
        return result;
    }


}
