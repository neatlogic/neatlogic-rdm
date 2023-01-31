/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.module.rdm.api.project;

import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ProjectStatusVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.RegexUtils;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveProjectStatusApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "保存项目状态";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "项目状态id，不提供代表添加"),
            @Param(name = "name", type = ApiParamType.REGEX, rule = RegexUtils.ENCHAR, isRequired = true, desc = "唯一标识"),
            @Param(name = "label", type = ApiParamType.STRING, isRequired = true, desc = "名称"),
            @Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "项目id"),
            @Param(name = "description", type = ApiParamType.STRING, desc = "说明"),
            @Param(name = "color", type = ApiParamType.STRING, desc = "颜色")})
    @Output({@Param(explode = ProjectStatusVo.class)})
    @Description(desc = "保存项目状态接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        ProjectStatusVo projectStatusVo = JSONObject.toJavaObject(paramObj, ProjectStatusVo.class);

        if (paramObj.getLong("id") == null) {
            List<ProjectStatusVo> statusList = projectMapper.getStatusByProjectId(projectStatusVo.getProjectId());
            projectStatusVo.setSort(statusList.size() + 1);
            projectMapper.insertProjectStatus(projectStatusVo);
        } else {
            projectMapper.updateProjectStatus(projectStatusVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/status/save";
    }
}
