/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.module.rdm.api.project;

import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ProjectStatusRelVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
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
