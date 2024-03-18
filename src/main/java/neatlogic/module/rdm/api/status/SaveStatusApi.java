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

package neatlogic.module.rdm.api.status;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppStatusVo;
import neatlogic.framework.rdm.dto.IssueConditionVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.RegexUtils;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveStatusApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "nmras.savestatusapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "nmras.savestatusapi.input.param.desc.id"),
            @Param(name = "name", type = ApiParamType.REGEX, rule = RegexUtils.ENCHAR, isRequired = true, desc = "common.uniquename"),
            @Param(name = "label", type = ApiParamType.STRING, isRequired = true, desc = "common.name"),
            @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "nmraa.getappapi.input.param.desc"),
            @Param(name = "description", type = ApiParamType.STRING, desc = "common.description"),
            @Param(name = "color", type = ApiParamType.STRING, desc = "common.color")})
    @Output({@Param(explode = AppStatusVo.class)})
    @Description(desc = "nmras.savestatusapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppStatusVo appStatusVo = JSONObject.toJavaObject(paramObj, AppStatusVo.class);

        if (paramObj.getLong("id") == null) {
            IssueConditionVo issueConditionVo = new IssueConditionVo();
            issueConditionVo.setAppId(appStatusVo.getAppId());
            List<AppStatusVo> statusList = appMapper.getStatusByAppId(issueConditionVo);
            appStatusVo.setSort(statusList.size() + 1);
            appMapper.insertAppStatus(appStatusVo);
        } else {
            appMapper.updateAppStatus(appStatusVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/status/save";
    }
}
