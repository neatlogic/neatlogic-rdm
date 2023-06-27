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

package neatlogic.module.rdm.api.apptype;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppTypeVo;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.rdm.enums.core.IAppType;
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
public class GetAllAppTypeApi extends PrivateApiComponentBase {


    @Override
    public String getName() {
        return "nmraa.getallappapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Output({@Param(explode = AppTypeVo[].class)})
    @Description(desc = "nmraa.getallappapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        List<AppTypeVo> appTypeList = new ArrayList<>();
        for (IAppType appType : AppTypeManager.getAppTypeList()) {
            AppTypeVo appTypeVo = new AppTypeVo();
            appTypeVo.setLabel(appType.getLabel());
            appTypeVo.setName(appType.getName());
            appTypeList.add(appTypeVo);
        }
        return appTypeList;
    }

    @Override
    public String getToken() {
        return "/rdm/apptype/get/all";
    }
}
