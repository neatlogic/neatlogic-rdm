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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppUserSettingVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveAttrUserSettingApi extends PrivateApiComponentBase {
    @Resource
    private AppMapper appMapper;


    @Override
    public String getName() {
        return "保存属性用户设置";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", type = ApiParamType.LONG, desc = "应用id", isRequired = true),
            @Param(name = "config", type = ApiParamType.JSONOBJECT, desc = "配置", isRequired = true)
    })
    @Description(desc = "保存属性用户设置接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppUserSettingVo settingVo = new AppUserSettingVo();
        settingVo.setAppId(paramObj.getLong("appId"));
        settingVo.setUserId(UserContext.get().getUserUuid(true));
        settingVo.setConfig(paramObj.getJSONObject("config"));
        appMapper.insertAppUserSetting(settingVo);
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/attr/usersetting/save";
    }
}
