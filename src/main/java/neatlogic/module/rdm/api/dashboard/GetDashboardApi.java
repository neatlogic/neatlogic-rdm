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

package neatlogic.module.rdm.api.dashboard;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.DashboardVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.DashboardNotFoundException;
import neatlogic.framework.rdm.exception.ProjectNotAuthDashboardException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.DashboardMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetDashboardApi extends PrivateApiComponentBase {

    @Resource
    private DashboardMapper dashboardMapper;


    @Override
    public String getToken() {
        return "/rdm/dashboard/get";
    }

    @Override
    public String getName() {
        return "nmrad.getdashboardapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc", isRequired = true),
            @Param(name = "id", type = ApiParamType.LONG, desc = "id", isRequired = true)})
    @Output({
            @Param(explode = DashboardVo.class),
    })
    @Description(desc = "nmrad.getdashboardapi.getname")
    @Override
    public Object myDoService(JSONObject jsonObj) {
        Long appId = jsonObj.getLong("appId");
        if (!ProjectAuthManager.checkAppAuth(appId, ProjectUserType.LEADER, ProjectUserType.OWNER, ProjectUserType.MEMBER)) {
            throw new ProjectNotAuthDashboardException();
        }
        Long id = jsonObj.getLong("id");
        DashboardVo dashboardVo = dashboardMapper.getDashboardById(id);
        if (dashboardVo == null) {
            throw new DashboardNotFoundException(id);
        }
        return dashboardVo;
    }
}
