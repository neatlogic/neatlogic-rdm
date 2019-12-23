package codedriver.module.rdm.api.projectiteration;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectIterationMapper;
import codedriver.module.rdm.dao.mapper.ProjectPriorityMapper;
import codedriver.module.rdm.dto.ProjectIterationVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProjectIterationGetApi
 * @Description 根据uuid查询项目迭代接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectIterationGetApi extends ApiComponentBase {

    @Resource
    private ProjectIterationMapper projectIterationMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectiteration/get";
    }

    @Override
    public String getName() {
        return "根据uuid查询项目迭代接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "迭代uuid", isRequired = true)
    })
    @Output({
            @Param(name = "projectIterationVo", type = ApiParamType.JSONOBJECT, desc = "projectIterationVo", explode = ProjectIterationVo.class)
    })
    @Description(desc = "根据uuid查询项目迭代接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        String uuid = jsonObj.getString("uuid");
        ProjectIterationVo projectIterationVo = projectIterationMapper.getProjectIterationByUuid(projectUuid, uuid);
        return projectIterationVo;
    }

}
