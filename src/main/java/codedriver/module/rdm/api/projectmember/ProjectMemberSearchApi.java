package codedriver.module.rdm.api.projectmember;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMemberMapper;
import codedriver.module.rdm.dto.ProjectMemberVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-23 18:21
 **/
@Service
public class ProjectMemberSearchApi extends ApiComponentBase {

    @Autowired
    private ProjectMemberMapper memberMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectmember/search";
    }

    @Override
    public String getName() {
        return "项目组成员列表返回接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true)})
    @Output({ @Param(name = "projectMemberList", explode = ProjectMemberVo[].class, desc = "项目成员列表")})
    @Description(desc = "项目组成员列表返回接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        JSONObject returnObj = new JSONObject();
        returnObj.put("projectMemberList", memberMapper.getProjectMemberList(projectUuid));
        return returnObj;
    }
}
