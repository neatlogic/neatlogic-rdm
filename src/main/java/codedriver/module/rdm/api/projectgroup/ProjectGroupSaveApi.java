/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectgroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.ProjectGroupVo;
import codedriver.module.rdm.services.ProjectGroupService;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 15:13
 **/
@Service
public class ProjectGroupSaveApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectGroupService groupService;

    @Override
    public String getToken() {
        return "module/rdm/projectgroup/save";
    }

    @Override
    public String getName() {
        return "项目组保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "uuid", type = ApiParamType.STRING, desc = "组uuid"),
             @Param( name = "name", type = ApiParamType.STRING, desc = "项目组名称", isRequired = true),
             @Param( name = "projectUuid", type = ApiParamType.STRING, desc = "项目Uuid", isRequired = true),
             @Param( name = "role", type = ApiParamType.STRING, isRequired = true)})
    @Output( @Param( name = "uuid", type = ApiParamType.STRING, desc = "组uuid"))
    @Description(desc = "项目组保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        ProjectGroupVo groupVo = new ProjectGroupVo();
        groupVo.setUuid(jsonObj.getString("uuid"));
        groupVo.setName(jsonObj.getString("name"));
        groupVo.setProjectUuid(jsonObj.getString("projectUuid"));
        groupVo.setRole(jsonObj.getString("role"));
        groupService.saveProjectGroup(groupVo);
        JSONObject returnObj = new JSONObject();
        returnObj.put("uuid", groupVo.getUuid());
        return returnObj;
    }
}
