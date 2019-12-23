package codedriver.module.rdm.api.projectiteration;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectIterationMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProjectIterationDeleteApi
 * @Description 删除项目迭代接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectIterationDeleteApi extends ApiComponentBase {

    @Resource
    private ProjectIterationMapper projectIterationMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectiteration/delete";
    }

    @Override
    public String getName() {
        return "删除项目迭代接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "项目迭代uuid", isRequired = true)})
    @Description(desc = "删除项目迭代接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        String uuid = jsonObj.getString("uuid");
        projectIterationMapper.deleteProjectIteration(projectUuid, uuid);
        return null;
    }


}
