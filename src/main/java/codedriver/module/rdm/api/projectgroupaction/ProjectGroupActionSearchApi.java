/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectgroupaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectGroupMemberMapper;
import codedriver.module.rdm.dto.ProjectGroupActionVo;
import codedriver.module.rdm.dto.ProjectGroupMemberVo;
import codedriver.module.rdm.services.ProjectRoleService;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 14:39
 **/
@Service
public class ProjectGroupActionSearchApi extends PrivateApiComponentBase {


    @Autowired
    private ProjectGroupMemberMapper memberMapper;

    @Autowired
    private ProjectRoleService roleService;

    @Override
    public String getToken() {
        return "module/rdm/projectgroupaction/search";
    }

    @Override
    public String getName() {
        return "获取用户权限接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "module", desc = "模块类型", type = ApiParamType.STRING, isRequired = true),
             @Param( name = "projectUuid", desc = "项目Uuid", type = ApiParamType.STRING, isRequired = true)})
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String module = jsonObj.getString("module");
        String projectUuid = jsonObj.getString("projectUuid");
        List<ProjectGroupMemberVo> memberList = memberMapper.getProjectGroupMember(projectUuid, UserContext.get().getUserId());
        if (memberList != null && memberList.size() > 0){
            JSONArray returnArray = new JSONArray();
            for (ProjectGroupMemberVo memberVo : memberList){
                List<ProjectGroupActionVo> actionVoList = roleService.searchProjectRoleAction(memberVo.getGroupUuid(), module);
                List<String> actionNameList = new ArrayList<>();
                actionVoList.stream().forEach(e -> actionNameList.add(e.getAction()));
                JSONObject dataObj = new JSONObject();
                dataObj.put("actionList", actionNameList);
                dataObj.put("member", memberVo);
                returnArray.add(dataObj);
            }
            JSONObject returnObj = new JSONObject();
            returnObj.put("actionArray", returnArray);
            return returnObj;
        }
        return new JSONObject();
    }
}
