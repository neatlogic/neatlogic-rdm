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

package neatlogic.module.rdm.api.project;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ProjectTemplateVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Output;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectTemplateMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListProjectTemplateApi extends PrivateApiComponentBase {

    @Resource
    private ProjectTemplateMapper projectTemplateMapper;

    @Override
    public String getName() {
        return "nmrap.listprojecttemplateapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Output({@Param(explode = ProjectTemplateVo.class)})
    @Description(desc = "nmrap.listprojecttemplateapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        ProjectTemplateVo projectTemplateVo = JSONObject.toJavaObject(paramObj, ProjectTemplateVo.class);
        return projectTemplateMapper.searchProjectTemplate(projectTemplateVo);
    }

    @Override
    public String getToken() {
        return "/rdm/projecttemplate/search";
    }
}
