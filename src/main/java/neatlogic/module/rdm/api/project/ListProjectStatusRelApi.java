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
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListProjectStatusRelApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "获取项目状态关系列表";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "项目id")})
    @Output({@Param(explode = ProjectStatusRelVo[].class)})
    @Description(desc = "获取项目状态关系列表接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        return projectMapper.getStatusRelByProjectId(paramObj.getLong("projectId"));
    }

    @Override
    public String getToken() {
        return "/rdm/project/statusrel/list";
    }
}
