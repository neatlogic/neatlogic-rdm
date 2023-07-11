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
import neatlogic.framework.rdm.auth.label.PROJECT_MANAGE;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = PROJECT_MANAGE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class OpenProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;


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
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/open";
    }
}
