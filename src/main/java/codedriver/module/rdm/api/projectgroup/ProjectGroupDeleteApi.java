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
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.ProjectGroupVo;
import codedriver.module.rdm.services.ProjectGroupService;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 16:06
 **/
@Service
public class ProjectGroupDeleteApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectGroupService groupService;

    @Override
    public String getToken() {
        return "module/rdm/projectgroup/delete";
    }

    @Override
    public String getName() {
        return "项目组删除接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "主键ID"),
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目Uuid")})
    @Description(desc = "项目组删除接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        ProjectGroupVo groupVo = new ProjectGroupVo();
        groupVo.setId(jsonObj.getLong("id"));
        groupVo.setProjectUuid(jsonObj.getString("projectUuid"));
        groupService.deleteProjectGroup(groupVo);
        return new JSONObject();
    }
}
