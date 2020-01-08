package codedriver.module.rdm.api.projectmember;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMemberMapper;
import codedriver.module.rdm.dto.ProjectMemberVo;
import com.alibaba.fastjson.JSONArray;
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
             @Param(name = "userIdList", type = ApiParamType.JSONARRAY, desc = "用户id集合", isRequired = true),
             @Param(name = "isLeader", type = ApiParamType.INTEGER, desc = "是否为组长, 1:是,0:不是", isRequired = true),
             @Param(name = "groupId", type = ApiParamType.LONG, desc = "成员组Id", isRequired = true)})
    @Output({ @Param(name = "idList", type = ApiParamType.JSONARRAY, desc = "主键ID集合")})
    @Description(desc = "项目成员保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject returnObj = new JSONObject();
        JSONArray idArray = new JSONArray();
        String projectUuid = jsonObj.getString("projectUuid");
        int isLeader = jsonObj.getInteger("isLeader");
        Long groupId = jsonObj.getLong("groupId");
        ProjectMemberVo member = new ProjectMemberVo();
        member.setProjectUuid(projectUuid);
        memberMapper.deleteProjectMember(member);
        JSONArray userArray = jsonObj.getJSONArray("userIdList");
        for (int i = 0; i < userArray.size(); i++){
            String userId = userArray.getString(i);
            ProjectMemberVo memberVo = new ProjectMemberVo();
            memberVo.setProjectUuid(projectUuid);
            memberVo.setUserId(userId);
            memberVo.setIsLeader(isLeader);
            memberVo.setGroupId(groupId);
            memberMapper.insertProjectMember(memberVo);
            idArray.add(memberVo.getId());
        }
        returnObj.put("idList", idArray);
        return returnObj;
    }
}
