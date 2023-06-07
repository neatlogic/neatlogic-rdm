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
import neatlogic.framework.common.dto.ValueTextVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Output;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Output({@Param(explode = ValueTextVo[].class)})
    @Description(desc = "nmraa.listprivateattrapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        List<AppAttrVo> attrList = new ArrayList<>();
        for (AttrType attrType : AttrType.values()) {
            if (attrType.isPrivate()) {
                AppAttrVo appAttrVo = new AppAttrVo();
                appAttrVo.setName(attrType.getName());
                appAttrVo.setLabel(attrType.getLabel());
                appAttrVo.setType(attrType.getType());
                appAttrVo.setIsRequired(0);
                appAttrVo.setIsPrivate(1);
                appAttrVo.setIsActive(1);
                attrList.add(appAttrVo);
            }
        }
        return attrList;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/privateattr/list";
    }
}
