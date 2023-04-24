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
public class GetAppStatusRelApi extends PrivateApiComponentBase {
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
        return "/rdm/app/statusrel/get";
    }
}
