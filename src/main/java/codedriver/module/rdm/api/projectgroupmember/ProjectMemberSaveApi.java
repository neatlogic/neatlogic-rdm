/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectgroupmember;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.services.ProjectGroupMemberService;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-23 18:45
 **/
@Service
public class ProjectMemberSaveApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectGroupMemberService memberService;

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

    @Input({ @Param(name = "userList", type = ApiParamType.JSONARRAY, desc = "用户id集合", isRequired = true),
             @Param(name = "groupUuid", type = ApiParamType.STRING, desc = "成员组Id", isRequired = true)})
    @Output({ @Param(name = "idList", type = ApiParamType.JSONARRAY, desc = "主键ID集合")})
    @Description(desc = "项目成员保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject returnObj = new JSONObject();
        String groupUuid = jsonObj.getString("groupUuid");
        JSONArray userArray = jsonObj.getJSONArray("userList");
        List<String> userList = new ArrayList<>();
        for (int i = 0; i < userArray.size(); i++){
            userList.add(userArray.getString(i));
        }
        returnObj.put("idList", memberService.saveProjectGroupMember(groupUuid, userList));
        return returnObj;
    }
}
