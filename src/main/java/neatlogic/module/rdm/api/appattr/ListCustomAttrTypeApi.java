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
import neatlogic.framework.rdm.attrhandler.code.AttrHandlerFactory;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
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
public class ListCustomAttrTypeApi extends PrivateApiComponentBase {


    @Override
    public String getName() {
        return "获取应用属性类型列表";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Output({@Param(explode = ValueTextVo[].class)})
    @Description(desc = "获取应用属性类型列表接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        List<ValueTextVo> dataList = new ArrayList<>();
        for (AttrType attrType : AttrType.values()) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(attrType.getType());
            if (handler != null && !handler.getIsPrivate()) {
                dataList.add(new ValueTextVo(attrType.getName(), attrType.getLabel()));
            }
        }
        return dataList;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/customattrtype/list";
    }
}
