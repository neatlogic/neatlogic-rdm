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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.DashboardVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.ProjectNotAuthDashboardException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.RegexUtils;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.DashboardMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveDashboardApi extends PrivateApiComponentBase {

    @Resource
    private DashboardMapper dashboardMapper;


    @Override
    public String getToken() {
        return "/rdm/dashboard/save";
    }

    @Override
    public String getName() {
        return "nmrad.savedashboardapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "nmrad.savedashboardapi.input.param.desc.id"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc", isRequired = true),
            @Param(name = "name", xss = true, type = ApiParamType.REGEX, rule = RegexUtils.NAME, desc = "common.name", isRequired = true),
            @Param(name = "widgetList", type = ApiParamType.JSONARRAY, desc = "组件列表，范例：\"chartType\": \"barchart\"," + "\"h\": 4," + "\"handler\": \"neatlogic.module.process.dashboard.handler.ProcessTaskDashboardHandler\"," + "\"i\": 0," + "\"name\": \"组件1\"," + "\"refreshInterval\": 3," + "\"uuid\": \"aaaa\"," + "\"w\": 5," + "\"x\": 0," + "\"y\": 0")})
    @Output({})
    @Description(desc = "nmrad.savedashboardapi.getname")
    @Override
    public Object myDoService(JSONObject jsonObj) {
        Long id = jsonObj.getLong("id");
        Long appId = jsonObj.getLong("appId");
        if (!ProjectAuthManager.checkAppAuth(appId, ProjectUserType.OWNER, ProjectUserType.LEADER, ProjectUserType.MEMBER)) {
            throw new ProjectNotAuthDashboardException();
        }
        DashboardVo dashboardVo = JSONObject.toJavaObject(jsonObj, DashboardVo.class);
        String userUuid = UserContext.get().getUserUuid(true);
        if (id == null) {
            dashboardVo.setFcu(userUuid);
            dashboardMapper.insertDashboard(dashboardVo);
        } else {
            dashboardVo.setLcu(userUuid);
            dashboardMapper.updateDashboard(dashboardVo);
        }
        return dashboardVo.getId();
    }

}
