package codedriver.module.rdm.api.projectgroupmember;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.framework.restful.annotation.Input;
import codedriver.module.rdm.dto.ProjectGroupMemberVo;
import codedriver.module.rdm.services.ProjectGroupMemberService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName App
 * @Description
 * @Auther
 * @Date 2019/12/11 17:36
 **/
@Service
public class ProjectMemberDeleteApi extends ApiComponentBase {

    @Autowired
    private ProjectGroupMemberService memberService;

    @Override
    public String getToken() {
        return "module/rdm/projectmember/delete";
    }

    @Override
    public String getName() {
        return "项目组成员删除接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param( name = "userId", type = ApiParamType.STRING, desc = "用户ID"),
            @Param( name = "groupUuid", type = ApiParamType.STRING, desc = "成员组UUID")})
    @Description(desc = "项目组成员删除接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String userId = jsonObj.getString("userId");
        String groupUuid = jsonObj.getString("groupUuid");
        ProjectGroupMemberVo memberVo = new ProjectGroupMemberVo();
        memberVo.setUserId(userId);
        memberVo.setGroupUuid(groupUuid);
        memberService.deleteProjectGroupMember(memberVo);
        return new JSONObject();
    }
}
