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
import java.util.ArrayList;
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
            @Param(name = "needSystemAttr", desc = "nmraa.searchprivateattrapi.input.param.desc.needsystemattr", rule = "0,1", type = ApiParamType.INTEGER)})
    @Output({@Param(explode = AppAttrVo[].class)})
    @Description(desc = "nmraa.searchprivateattrapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppAttrVo appAttrVo = JSONObject.toJavaObject(paramObj, AppAttrVo.class);
        List<AppAttrVo> attrList = attrMapper.searchAppAttr(appAttrVo);
        Integer needSystemAttr = paramObj.getInteger("needSystemAttr");
        if (needSystemAttr != null && needSystemAttr.equals(1)) {
            List<AppAttrVo> systemAttrList = new ArrayList<>();
            for (SystemAttrType attrType : SystemAttrType.values()) {
                AppAttrVo systemAttrVo = new AppAttrVo();
                systemAttrVo.setAppId(appAttrVo.getAppId());
                systemAttrVo.setId(0L);//避免自动分配id
                systemAttrVo.setType(attrType.getType());
                systemAttrVo.setName(attrType.getName());
                systemAttrVo.setLabel(attrType.getLabel());
                systemAttrVo.setTypeText(attrType.getTypeText());
                systemAttrList.add(systemAttrVo);
            }
            attrList.addAll(0, systemAttrList);
        }
        return attrList;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/attr/search";
    }
}
