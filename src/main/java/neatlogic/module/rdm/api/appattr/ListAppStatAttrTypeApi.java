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

package neatlogic.module.rdm.api.appattr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.enums.AppStatAttrType;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListAppStatAttrTypeApi extends PrivateApiComponentBase {
    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "获取RDM应用统计属性类型列表";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc"),
            @Param(name = "appType", type = ApiParamType.STRING, desc = "term.rdm.apptype")})
    @Output({@Param(explode = JSONObject[].class)})
    @Description(desc = "获取RDM应用统计属性类型列表接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        String appType = paramObj.getString("appType");
        Long appId = paramObj.getLong("appId");
        if (StringUtils.isBlank(appType) && appId != null) {
            AppVo appVo = appMapper.getAppById(appId);
            if (appVo != null) {
                appType = appVo.getType();
            }
        }
        JSONArray resultList = new JSONArray();
        for (AppStatAttrType statAttrType : AppStatAttrType.getListByAppType(appType)) {
            if (statAttrType.getAllowCustomAttr()) {
                resultList.add(statAttrType.toJson());
            }
        }
        return resultList;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/statattrtype/list";
    }
}
