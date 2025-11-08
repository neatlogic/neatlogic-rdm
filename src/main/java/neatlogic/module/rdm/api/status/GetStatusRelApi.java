/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.module.rdm.api.status;

import com.alibaba.fastjson.JSON;
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
    @Description(desc = "获取状态关系")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppStatusRelVo appStatusRelVo = JSON.toJavaObject(paramObj, AppStatusRelVo.class);
        return appMapper.getAppStatusRel(appStatusRelVo);
    }

    @Override
    public String getToken() {
        return "/rdm/statusrel/get";
    }
}
