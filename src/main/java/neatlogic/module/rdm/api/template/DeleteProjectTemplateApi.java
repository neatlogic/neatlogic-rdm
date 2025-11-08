/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.module.rdm.api.template;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.PROJECT_TEMPLATE_MANAGE;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectTemplateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = PROJECT_TEMPLATE_MANAGE.class)
@OperationType(type = OperationTypeEnum.DELETE)
@Transactional
public class DeleteProjectTemplateApi extends PrivateApiComponentBase {

    @Resource
    private ProjectTemplateMapper projectTemplateMapper;


    @Override
    public String getName() {
        return "nmrat.deleteprojecttemplateapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "common.templateid", isRequired = true, type = ApiParamType.LONG)})
    @Description(desc = "nmrat.deleteprojecttemplateapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        projectTemplateMapper.deleteProjectTemplate(paramObj.getLong("id"));
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/projecttemplate/delete";
    }
}
