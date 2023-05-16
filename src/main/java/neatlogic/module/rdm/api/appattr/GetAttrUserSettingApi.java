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

package neatlogic.module.rdm.api.appattr;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetAttrUserSettingApi extends PrivateApiComponentBase {
    @Resource
    private AppMapper appMapper;


    @Override
    public String getName() {
        return "获取属性用户设置";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", type = ApiParamType.LONG, desc = "应用id", isRequired = true)
    })
    @Description(desc = "获取属性用户设置接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        return appMapper.getAppUserSetting(UserContext.get().getUserUuid(true), paramObj.getLong("appId"));
    }

    @Override
    public String getToken() {
        return "/rdm/attr/usersetting/get";
    }
}
