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
import codedriver.module.rdm.dto.ProjectGroupActionVo;
import codedriver.module.rdm.services.ProjectRoleService;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-08 11:01
 **/
@Service
public class ProjectGroupActionSaveApi extends PrivateApiComponentBase {


    @Autowired
    private ProjectRoleService roleService;

    @Override
    public String getToken() {
        return "module/rdm/projectgroupaction/save";
    }

    @Override
    public String getName() {
        return "项目组权限添加接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "groupUuid", type = ApiParamType.STRING, desc = "组uuid", isRequired = true),
             @Param( name = "moduleList", type = ApiParamType.JSONARRAY, desc = "模块权限操作集合", isRequired = true)})
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String groupUuid = jsonObj.getString("groupUuid");
        JSONArray actionArray = jsonObj.getJSONArray("moduleList");
        List<ProjectGroupActionVo> roleActionVoList = new ArrayList<>();
        for (int i = 0 ; i < actionArray.size(); i++){
            JSONObject obj = actionArray.getJSONObject(i);
            String module = obj.getString("module");
            String processAreaUuid = "";
            if (obj.containsKey("processAreaUuid")){
                processAreaUuid = obj.getString("processAreaUuid");
            }
            JSONArray actionList = obj.getJSONArray("actionList");
            for (int j = 0; j < actionList.size(); j++){
                ProjectGroupActionVo actionVo = new ProjectGroupActionVo();
                actionVo.setProcessAreaUuid(processAreaUuid);
                actionVo.setAction(actionList.getString(j));
                actionVo.setGroupUuid(groupUuid);
                actionVo.setCreateUser(UserContext.get().getUserId());
                actionVo.setModule(module);
                roleActionVoList.add(actionVo);
            }
        }
        roleService.saveProjectRoleAction(groupUuid, roleActionVoList);
        return new JSONObject();
    }
}
