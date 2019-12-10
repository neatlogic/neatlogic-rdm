package codedriver.module.rdm.api.project;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProcessAreaMapper;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;

/**
 * @ClassName ProjectGetApi
 * @Description
 * @Auther fandong
 * @Date 2019/12/10 17:31
 **/
public class ProjectGetApi extends ApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getToken() {
        return "module/rdm/project/get";
    }

    @Override
    public String getName() {
        return "根据查询uuid项目接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param(name = "uuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
             @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = false)
    })

    @Description(desc = "根据查询uuid项目接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String projectUuid = jsonObj.getString("uuid");
        result.put("processArea", projectMapper.getProjectByUuid(projectUuid));
        if(jsonObj.containsKey("processAreaUuid")){
            String processAreaUuid = jsonObj.getString("processAreaUuid");
            result.put("fieldList", projectMapper.getProjectFieldList(projectUuid, processAreaUuid));
        }
        return result;
    }
}
