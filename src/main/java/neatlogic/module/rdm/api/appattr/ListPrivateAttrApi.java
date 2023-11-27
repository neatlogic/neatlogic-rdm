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
import neatlogic.framework.common.dto.ValueTextVo;
import neatlogic.framework.rdm.attrhandler.code.AttrHandlerFactory;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.enums.SystemAttrType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListPrivateAttrApi extends PrivateApiComponentBase {


    @Override
    public String getName() {
        return "nmraa.listprivateattrapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "appType", desc = "term.dr.drapptype.getenumname", type = ApiParamType.STRING),
            @Param(name = "needSystemAttr", desc = "nmraa.searchprivateattrapi.input.param.desc.needsystemattr", rule = "0,1", type = ApiParamType.INTEGER)})
    @Output({@Param(explode = ValueTextVo[].class)})
    @Description(desc = "nmraa.listprivateattrapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        String appType = paramObj.getString("appType");
        List<AppAttrVo> attrList = new ArrayList<>();
        Integer needSystemAttr = paramObj.getInteger("needSystemAttr");
        AttrType[] appAttrList = AppTypeManager.getAttrList(appType);
        for (AttrType attrType : AttrType.values()) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(attrType.getType());
            if (handler != null && handler.getIsPrivate()) {
                if (StringUtils.isBlank(appType) || (appAttrList != null && Arrays.stream(appAttrList).anyMatch(d -> d.getType().equals(attrType.getType())))) {
                    AppAttrVo appAttrVo = new AppAttrVo();
                    appAttrVo.setName(attrType.getName());
                    appAttrVo.setLabel(attrType.getLabel());
                    appAttrVo.setType(attrType.getType());
                    appAttrVo.setIsRequired(0);
                    appAttrVo.setIsPrivate(1);
                    appAttrVo.setIsActive(0);
                    attrList.add(appAttrVo);
                }
            }
        }
        if (needSystemAttr != null && needSystemAttr.equals(1)) {
            List<AppAttrVo> systemAttrList = SystemAttrType.getSystemAttrList(null);
            attrList.addAll(0, systemAttrList);
        }
        return attrList;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/privateattr/list";
    }
}
