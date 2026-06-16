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
import neatlogic.framework.exception.type.ParamIrregularException;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppStatusRelVo;
import neatlogic.framework.rdm.dto.AppStatusVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class ToggleStatusRelApi extends PrivateApiComponentBase {

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
    @Description(desc = "添加或删除应用状态流转关系")
    @Override
    public Object myDoService(JSONObject paramObj) {
        String action = paramObj.getString("action");
        AppStatusRelVo appStatusRelVo = JSON.toJavaObject(paramObj, AppStatusRelVo.class);
        if (action.equals("add")) {
            validateScope(appStatusRelVo);
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

    private void validateScope(AppStatusRelVo appStatusRelVo) {
        if (appStatusRelVo.getFromStatusId() == null || appStatusRelVo.getFromStatusId().equals(0L)) {
            return;
        }
        AppStatusVo fromStatus = appMapper.getStatusById(appStatusRelVo.getFromStatusId());
        AppStatusVo toStatus = appMapper.getStatusById(appStatusRelVo.getToStatusId());
        if (fromStatus == null || toStatus == null) {
            return;
        }
        String fromScope = fromStatus.getScope();
        String toScope = toStatus.getScope();
        if (!"all".equals(fromScope) && !"all".equals(toScope) && !fromScope.equals(toScope)) {
            throw new ParamIrregularException("toStatusId");
        }
    }

    @Override
    public String getToken() {
        return "/rdm/statusrel/toggle";
    }
}
