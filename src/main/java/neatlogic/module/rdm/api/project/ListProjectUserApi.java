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
import neatlogic.framework.rdm.dto.ProjectUserVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListProjectUserApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "nmrap.listprojectuserapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.projectid"),
            @Param(name = "userType", type = ApiParamType.ENUM, member = ProjectUserType.class, isRequired = true, desc = "common.usertype")})
    @Output({@Param(explode = ProjectUserVo[].class)})
    @Description(desc = "nmrap.listprojectuserapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        ProjectUserVo projectUserVo = JSONObject.toJavaObject(paramObj, ProjectUserVo.class);
        return projectMapper.getProjectUserList(projectUserVo);
    }

    @Override
    public String getToken() {
        return "/rdm/project/user/list";
    }
}
