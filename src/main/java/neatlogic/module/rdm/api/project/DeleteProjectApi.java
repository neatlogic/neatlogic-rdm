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
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.exception.CreateObjectSchemaException;
import neatlogic.framework.rdm.exception.ProjectNotAuthDeleteException;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.DELETE)
@Transactional
public class DeleteProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AppMapper appMapper;


    @Resource
    private ProjectService projectService;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "nmrap.deleteprojectapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "term.rdm.projectid", isRequired = true, type = ApiParamType.LONG)})
    @Description(desc = "nmrap.deleteprojectapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("id");
        ProjectVo projectVo = projectMapper.getProjectById(projectId);
        if (projectVo == null) {
            throw new ProjectNotFoundException(projectId);
        }
        if (!projectVo.getIsOwner()) {
            throw new ProjectNotAuthDeleteException(projectVo.getName());
        }
        List<AppVo> appList = appMapper.getAppDetailByProjectId(projectId, null);
        for (AppVo appVo : appList) {
            issueMapper.deleteIssueByAppId(appVo);
            appMapper.deleteAppById(appVo.getId());
            EscapeTransactionJob.State s = projectService.dropObjectSchema(appVo);
            if (!s.isSucceed()) {
                throw new CreateObjectSchemaException(appVo.getName());
            }
        }
        projectMapper.deleteProjectById(projectId);
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/delete";
    }
}
