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
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.DashboardVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.DashboardMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchDashboardApi extends PrivateApiComponentBase {

    @Resource
    private DashboardMapper dashboardMapper;


    @Override
    public String getToken() {
        return "/rdm/dashboard/search";
    }

    @Override
    public String getName() {
        return "nmda.searchdashboardapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc", isRequired = true),
            @Param(name = "keyword", type = ApiParamType.STRING, desc = "common.keyword"),
            @Param(name = "isActive", type = ApiParamType.INTEGER, desc = "common.isactive"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "common.currentpage"),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "common.pagesize"),
            @Param(name = "needPage", type = ApiParamType.BOOLEAN, desc = "common.isneedpage")})
    @Output({
            @Param(explode = BasePageVo.class),
            @Param(name = "tbodyList", explode = DashboardVo[].class, desc = "common.tbodylist")
    })
    @Description(desc = "nmda.searchdashboardapi.getname")
    @Override
    public Object myDoService(JSONObject jsonObj) {
        DashboardVo dashboardVo = JSONObject.toJavaObject(jsonObj, DashboardVo.class);
        int rowNum = dashboardMapper.searchDashboardCount(dashboardVo);
        List<DashboardVo> dashboardList = null;
        if (rowNum > 0) {
            dashboardVo.setRowNum(rowNum);
            dashboardList = dashboardMapper.searchDashboard(dashboardVo);
        }

        return TableResultUtil.getResult(dashboardList, dashboardVo);
    }
}
