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
