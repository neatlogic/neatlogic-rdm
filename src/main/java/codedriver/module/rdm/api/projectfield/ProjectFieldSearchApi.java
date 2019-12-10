package codedriver.module.rdm.api.projectfield;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.FieldMapper;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dto.FieldVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProjectFieldSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectFieldSearchApi extends ApiComponentBase {

    @Resource
    private FieldMapper fieldMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectfield/search";
    }

    @Override
    public String getName() {
        return "查询项目属性接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true)
    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        List<FieldVo> projectFieldList = projectMapper.getProjectFieldList(projectUuid, processAreaUuid);
        result.put("fieldList", projectFieldList);
        return result;
    }

}
