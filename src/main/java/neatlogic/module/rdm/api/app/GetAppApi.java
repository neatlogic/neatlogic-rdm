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

package neatlogic.module.rdm.api.app;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.enums.SystemAttrType;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetAppApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "nmraa.getappapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, isRequired = true, desc = "nmraa.getappapi.input.param.desc"),
            @Param(name = "needSystemAttr", desc = "nmraa.searchprivateattrapi.input.param.desc.needsystemattr", rule = "0,1", type = ApiParamType.INTEGER)})
    @Output({@Param(explode = AppVo.class)})
    @Description(desc = "nmraa.getappapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppVo appVo = appMapper.getAppById(paramObj.getLong("id"));
        Integer needSystemAttr = paramObj.getInteger("needSystemAttr");
        if (needSystemAttr != null && needSystemAttr.equals(1)) {
            List<AppAttrVo> systemAttrList = SystemAttrType.getSystemAttrList(appVo.getId());
            if (appVo.getAttrList() != null) {
                appVo.getAttrList().addAll(0, systemAttrList);
            } else {
                appVo.setAttrList(systemAttrList);
            }
        }
        return appVo;
    }

    @Override
    public String getToken() {
        return "/rdm/app/get";
    }
}
