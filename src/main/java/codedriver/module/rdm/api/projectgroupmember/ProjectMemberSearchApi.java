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
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.ProjectGroupMemberVo;
import codedriver.module.rdm.services.ProjectGroupMemberService;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-23 18:21
 **/
@Service
public class ProjectMemberSearchApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectGroupMemberService memberService;

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
    @Output({ @Param(name = "projectMemberList", explode = ProjectGroupMemberVo[].class, desc = "项目成员列表")})
    @Description(desc = "项目组成员列表返回接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        JSONObject returnObj = new JSONObject();
        returnObj.put("projectMemberList", memberService.searchProjectGroupMemberList(projectUuid));
        return returnObj;
    }
}
