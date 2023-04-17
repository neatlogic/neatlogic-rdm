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

package neatlogic.module.rdm.api.app;

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
public class UpdateAppStatusTypeApi extends PrivateApiComponentBase {

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
        return "/rdm/app/statustype/update";
    }
}
