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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.enums.SystemAttrType;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchAttrApi extends PrivateApiComponentBase {
    @Resource
    private AttrMapper attrMapper;

    @Override
    public String getName() {
        return "nmraa.searchprivateattrapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", desc = "nmraa.getappapi.input.param.desc", isRequired = true, type = ApiParamType.LONG),
            @Param(name = "isActive", desc = "common.isactive", type = ApiParamType.INTEGER, rule = "0,1"),
            @Param(name = "needSystemAttr", desc = "nmraa.searchprivateattrapi.input.param.desc.needsystemattr", rule = "0,1", type = ApiParamType.INTEGER)})
    @Output({@Param(explode = AppAttrVo[].class)})
    @Description(desc = "nmraa.searchprivateattrapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppAttrVo appAttrVo = JSONObject.toJavaObject(paramObj, AppAttrVo.class);
        List<AppAttrVo> attrList = attrMapper.searchAppAttr(appAttrVo);
        Integer needSystemAttr = paramObj.getInteger("needSystemAttr");
        if (needSystemAttr != null && needSystemAttr.equals(1)) {
            List<AppAttrVo> systemAttrList = SystemAttrType.getSystemAttrList(appAttrVo.getAppId());
            attrList.addAll(0, systemAttrList);
        }
        return attrList;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/attr/search";
    }
}
