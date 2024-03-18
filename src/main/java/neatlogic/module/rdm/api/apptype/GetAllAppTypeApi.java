/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
