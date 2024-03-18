/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.module.rdm.api.project;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.PROJECT_TEMPLATE_MANAGE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.dao.mapper.ProjectTemplateMapper;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AuthAction(action = PROJECT_TEMPLATE_MANAGE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveProjectTemplateFromProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectTemplateMapper projectTemplateMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "nmrap.saveprojecttemplatefromprojectapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", desc = "term.rdm.projectid", isRequired = true, type = ApiParamType.LONG),
            @Param(name = "templateName", desc = "common.templatename", isRequired = true, type = ApiParamType.STRING)})
    @Description(desc = "nmrap.saveprojecttemplatefromprojectapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("projectId");
        ProjectVo projectVo = projectMapper.getProjectById(projectId);
        if (projectVo == null) {
            throw new ProjectNotFoundException(projectId);
        }
        String templateName = paramObj.getString("templateName");
        List<AppAttrVo> appAttrList = projectMapper.getAppAttrByProjectId(projectId);
        List<AppStatusVo> statusList = projectMapper.getAppStatusByProjectId(projectId);
        List<AppStatusRelVo> statusRelList = projectMapper.getAppStatusRelByProjectId(projectId);
        //模板需要去掉和用户相关的配置，因为用户来源依赖项目id
        statusRelList.forEach(rel -> {
            if (MapUtils.isNotEmpty(rel.getConfig())) {
                rel.getConfig().remove("authList");
                rel.getConfig().remove("userList");
            }
        });
        ProjectTemplateVo projectTemplateVo = new ProjectTemplateVo();
        projectTemplateVo.setName(templateName);
        projectTemplateVo.setIsActive(1);
        projectTemplateMapper.insertProjectTemplate(projectTemplateVo);
        for (int i = 0; i < projectVo.getAppList().size(); i++) {
            JSONObject config = new JSONObject();
            AppVo appVo = projectVo.getAppList().get(i);
            config.put("attrList", appAttrList.stream().filter(d -> d.getAppId().equals(appVo.getId())).collect(Collectors.toList()));
            config.put("statusList", statusList.stream().filter(d -> d.getAppId().equals(appVo.getId())).collect(Collectors.toList()));
            config.put("statusRelList", statusRelList.stream().filter(d -> d.getAppId().equals(appVo.getId())).collect(Collectors.toList()));
            ProjectTemplateAppTypeVo appTypeVo = new ProjectTemplateAppTypeVo();
            appTypeVo.setAppType(appVo.getType());
            appTypeVo.setTemplateId(projectTemplateVo.getId());
            appTypeVo.setSort(i + 1);
            appTypeVo.setConfig(config);
            projectTemplateMapper.insertProjectTemplateAppType(appTypeVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/projecttemplate/savefromproject";
    }
}
