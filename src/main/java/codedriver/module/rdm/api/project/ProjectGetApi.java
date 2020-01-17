package codedriver.module.rdm.api.project;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dto.ProjectVo;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;

/**
 * @ClassName ProjectGetApi
 * @Description
 * @Auther
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
        return "根据uuid查询项目接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = false)
    })
    @Output({
            @Param(name = "project", type = ApiParamType.JSONOBJECT, desc = "项目信息", explode = ProjectVo.class)
    })
    @Description(desc = "根据uuid查询项目接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String projectUuid = jsonObj.getString("uuid");
        result.put("project", projectMapper.getProjectByUuid(projectUuid));
        return result;
    }
}
