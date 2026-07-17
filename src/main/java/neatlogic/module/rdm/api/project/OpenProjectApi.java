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
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.PROJECT_MANAGE;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.framework.rdm.notify.constvalue.RdmProjectNotifyTriggerType;
import neatlogic.framework.rdm.notify.dto.RdmNotifyContextVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.notify.service.RdmNotifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = PROJECT_MANAGE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class OpenProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private RdmNotifyService rdmNotifyService;


    @Override
    public String getName() {
        return "nmrap.openprojectapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "term.rdm.projectid", isRequired = true, type = ApiParamType.LONG)})
    @Description(desc = "nmrap.openprojectapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("id");
        ProjectVo projectVo = projectMapper.getProjectById(projectId);
        if (projectVo == null) {
            throw new ProjectNotFoundException(projectId);
        }
        projectVo.setIsClose(0);
        projectMapper.updateProject(projectVo);
        RdmNotifyContextVo contextVo = new RdmNotifyContextVo();
        contextVo.setBizType("project");
        contextVo.setProjectVo(projectVo);
        rdmNotifyService.notify(contextVo, RdmProjectNotifyTriggerType.OPENED);
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/open";
    }
}
