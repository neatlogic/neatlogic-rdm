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
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppStatusVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class UpdateStatusTypeApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "修改应用状态类型";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, isRequired = true, desc = "对象状态id"),
            @Param(name = "type", type = ApiParamType.ENUM, rule = "start,end", isRequired = true, desc = "状态类型，start或end"),
            @Param(name = "flag", type = ApiParamType.BOOLEAN, isRequired = true, desc = "类型状态")
    })
    @Output({@Param(explode = AppStatusVo.class)})
    @Description(desc = "修改应用状态类型接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        String type = paramObj.getString("type");
        Boolean flag = paramObj.getBoolean("flag");
        AppStatusVo appStatusVo = appMapper.getStatusById(id);
        if (appStatusVo != null) {
            if (type.equalsIgnoreCase("start")) {
                if (flag) {
                    appStatusVo.setIsStart(1);
                    appMapper.resetAppStatusIsStart(appStatusVo.getAppId());
                } else {
                    appStatusVo.setIsStart(0);
                }
            } else {
                if (flag) {
                    appStatusVo.setIsEnd(1);
                } else {
                    appStatusVo.setIsEnd(0);
                }
            }
            appMapper.updateAppStatusType(appStatusVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/statustype/update";
    }
}
