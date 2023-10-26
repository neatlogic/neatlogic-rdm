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

package neatlogic.module.rdm.api.template;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.PROJECT_TEMPLATE_MANAGE;
import neatlogic.framework.rdm.dto.ProjectTemplateAppTypeVo;
import neatlogic.framework.rdm.dto.ProjectTemplateVo;
import neatlogic.framework.rdm.exception.AppTypeIsEmptyException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectTemplateMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = PROJECT_TEMPLATE_MANAGE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveProjectTemplateApi extends PrivateApiComponentBase {

    @Resource
    private ProjectTemplateMapper projectTemplateMapper;


    @Override
    public String getName() {
        return "nmrat.saveprojecttemplateapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "common.templateid", type = ApiParamType.LONG),
            @Param(name = "name", desc = "common.name", isRequired = true, maxLength = 50, xss = true, type = ApiParamType.STRING),
            @Param(name = "isActive", desc = "common.isactive", defaultValue = "0", rule = "0,1", type = ApiParamType.INTEGER),
            @Param(name = "appTypeList", desc = "nfrd.projecttemplatevo.entityfield.name.apptypelist", isRequired = true, type = ApiParamType.JSONARRAY)})
    @Output({@Param(name = "id", desc = "common.templateid", type = ApiParamType.LONG)})
    @Description(desc = "nmrat.saveprojecttemplateapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        ProjectTemplateVo projectTemplateVo = JSONObject.toJavaObject(paramObj, ProjectTemplateVo.class);
        if (projectTemplateVo.getAppTypeList().isEmpty()) {
            throw new AppTypeIsEmptyException();
        }
        if (id == null) {
            projectTemplateMapper.insertProjectTemplate(projectTemplateVo);
        } else {
            projectTemplateMapper.updateProjectTemplate(projectTemplateVo);
            projectTemplateMapper.deleteProjectTemplateAppTypeByTemplateId(projectTemplateVo.getId());
        }
        if (CollectionUtils.isNotEmpty(projectTemplateVo.getAppTypeList())) {
            int sort = 1;
            for (ProjectTemplateAppTypeVo appTypeVo : projectTemplateVo.getAppTypeList()) {
                appTypeVo.setTemplateId(projectTemplateVo.getId());
                appTypeVo.setSort(sort);
                projectTemplateMapper.insertProjectTemplateAppType(appTypeVo);
                sort += 1;
            }
        }
        return projectTemplateVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/projecttemplate/save";
    }
}
