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
