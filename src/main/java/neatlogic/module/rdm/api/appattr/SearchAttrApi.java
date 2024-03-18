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
