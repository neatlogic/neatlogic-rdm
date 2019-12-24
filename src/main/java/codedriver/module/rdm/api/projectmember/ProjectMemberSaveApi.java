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
 * @create: 2019-12-23 18:45
 **/
@Service
public class ProjectMemberSaveApi extends ApiComponentBase {

    @Autowired
    private ProjectMemberMapper memberMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectmember/save";
    }

    @Override
    public String getName() {
        return "项目成员保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
             @Param(name = "userId", type = ApiParamType.STRING, desc = "用户id", isRequired = true),
             @Param(name = "id", type = ApiParamType.LONG, desc = "主键ID")})
    @Output({ @Param(name = "id", type = ApiParamType.LONG, desc = "主键ID")})
    @Description(desc = "项目成员保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        String userId = jsonObj.getString("userId");
        ProjectMemberVo memberVo = new ProjectMemberVo();
        memberVo.setProjectUuid(projectUuid);
        memberVo.setUserId(userId);
        if (jsonObj.containsKey("id")){
            memberVo.setId(jsonObj.getLong("id"));
            memberMapper.updateProjectMember(memberVo);
        }else {
            memberMapper.insertProjectMember(memberVo);
        }
        JSONObject returnObj = new JSONObject();
        returnObj.put("id", memberVo.getId());
        return returnObj;
    }
}
