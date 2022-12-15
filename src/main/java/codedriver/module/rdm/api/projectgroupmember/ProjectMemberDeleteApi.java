/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectgroupmember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.ProjectGroupMemberVo;
import codedriver.module.rdm.services.ProjectGroupMemberService;

/**
 * @ClassName App
 * @Description
 * @Auther
 * @Date 2019/12/11 17:36
 **/
@Service
public class ProjectMemberDeleteApi extends PrivateApiComponentBase {

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
