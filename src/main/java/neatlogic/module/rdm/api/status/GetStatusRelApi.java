/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.module.rdm.api.status;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.ValueTextVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppStatusRelVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetStatusRelApi extends PrivateApiComponentBase {
    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "获取状态关系";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", desc = "应用id", isRequired = true, type = ApiParamType.LONG),
            @Param(name = "fromStatusId", desc = "来源状态", isRequired = true, type = ApiParamType.LONG),
            @Param(name = "toStatusId", desc = "目标状态", isRequired = true, type = ApiParamType.LONG)})
    @Output({@Param(explode = ValueTextVo[].class)})
    @Description(desc = "获取状态关系接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppStatusRelVo appStatusRelVo = JSONObject.toJavaObject(paramObj, AppStatusRelVo.class);
        return appMapper.getAppStatusRel(appStatusRelVo);
    }

    @Override
    public String getToken() {
        return "/rdm/statusrel/get";
    }
}
