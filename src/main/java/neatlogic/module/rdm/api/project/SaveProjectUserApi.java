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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ProjectUserVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveProjectUserApi extends PrivateApiComponentBase {
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "nmrap.saveprojectuserapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, desc = "term.rdm.projectid", isRequired = true),
            @Param(name = "userType", type = ApiParamType.STRING, desc = "common.usertype", isRequired = true),
            @Param(name = "userIdList", type = ApiParamType.JSONARRAY, desc = "common.userlist", isRequired = true)})
    @Description(desc = "nmrap.saveprojectuserapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        JSONArray userIdList = paramObj.getJSONArray("userIdList");
        String userType = paramObj.getString("userType");
        Long projectId = paramObj.getLong("projectId");
        for (int i = 0; i < userIdList.size(); i++) {
            String userId = userIdList.getString(i).replace("user#", "");
            ProjectUserVo projectUserVo = new ProjectUserVo();
            projectUserVo.setUserId(userId);
            projectUserVo.setProjectId(projectId);
            projectUserVo.setUserType(userType);
            projectMapper.insertProjectUser(projectUserVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/user/save";
    }
}
