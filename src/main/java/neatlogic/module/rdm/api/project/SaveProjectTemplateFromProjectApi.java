/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
