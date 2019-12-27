package codedriver.module.rdm.api.projectprocessarea;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.SystemFieldMapper;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dto.FieldVo;
import codedriver.module.rdm.dto.ProjectProcessAreaVo;
import codedriver.module.rdm.services.ProjectService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectFieldSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectProcessAreaSearchApi extends ApiComponentBase {

    @Autowired
    private ProjectService projectService;

    @Override
    public String getToken() {
        return "module/rdm/projectprocessarea/search";
    }

    @Override
    public String getName() {
        return "查询项目过程域接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
             @Param( name = "processAreaUuid", type = ApiParamType.STRING, desc = "模板uuid")})
    @Output({ @Param(name = "processAreaList", explode = ProjectProcessAreaVo[].class, desc = "项目过程域集合")})
    @Description(desc = "查询项目过程域接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject returnObj = new JSONObject();
        ProjectProcessAreaVo areaVo = new ProjectProcessAreaVo();
        areaVo.setProjectUuid(jsonObj.getString("projectUuid"));
        if (jsonObj.containsKey("processAreaUuid")){
           areaVo.setProcessAreaUuid(jsonObj.getString("processAreaUuid"));
        }
        returnObj.put("processAreaList", projectService.searchProjectProcessArea(areaVo));
        return returnObj;
    }

}
