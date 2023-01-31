/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.project;

import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.dto.ProjectStatusRelVo;
import codedriver.framework.restful.annotation.*;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveProjectStatusRelApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "修改状态流转关系";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "fromStatusId", type = ApiParamType.LONG, isRequired = true, desc = "来源状态id"),
            @Param(name = "toStatusId", type = ApiParamType.LONG, isRequired = true, desc = "目标状态id"),
            @Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "项目id"),
            @Param(name = "action", type = ApiParamType.ENUM, rule = "add,delete", desc = "动作")})
    @Output({@Param(explode = ProjectStatusRelVo[].class)})
    @Description(desc = "修改状态流转关系接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        String action = paramObj.getString("action");
        ProjectStatusRelVo projectStatusRelVo = JSONObject.toJavaObject(paramObj, ProjectStatusRelVo.class);
        if (action.equals("add")) {
            projectMapper.insertProjectStatusRel(projectStatusRelVo);
        } else if (action.equals("delete")) {
            projectMapper.deleteProjectStatusRel(projectStatusRelVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/statusrel/save";
    }
}
