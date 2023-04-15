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
import neatlogic.framework.rdm.dto.AppStatusRelVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class ToggleAppStatusRelApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "添加或删除应用状态流转关系";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "fromStatusId", type = ApiParamType.LONG, isRequired = true, desc = "来源状态id"),
            @Param(name = "toStatusId", type = ApiParamType.LONG, isRequired = true, desc = "目标状态id"),
            @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id"),
            @Param(name = "action", type = ApiParamType.ENUM, rule = "add,delete", desc = "动作")})
    @Output({@Param(type = ApiParamType.LONG, desc = "关系id")})
    @Description(desc = "添加或删除应用状态流转关系接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        String action = paramObj.getString("action");
        AppStatusRelVo appStatusRelVo = JSONObject.toJavaObject(paramObj, AppStatusRelVo.class);
        if (action.equals("add")) {
            appMapper.insertAppStatusRel(appStatusRelVo);
            return appStatusRelVo.getId();
        } else if (action.equals("delete")) {
            AppStatusRelVo oldVo = appMapper.getAppStatusRel(appStatusRelVo);
            if (oldVo != null) {
                appMapper.deleteAppStatusRel(appStatusRelVo);
                return oldVo.getId();
            }
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/app/statusrel/toggle";
    }
}
