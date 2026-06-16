/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.module.rdm.api.status;

import com.alibaba.fastjson.JSON;
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
            @Param(name = "scope", type = ApiParamType.ENUM, rule = "original,copy,all", desc = "状态作用范围"),
            @Param(name = "color", type = ApiParamType.STRING, desc = "common.color")})
    @Output({@Param(explode = AppStatusVo.class)})
    @Description(desc = "nmras.savestatusapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppStatusVo appStatusVo = JSON.toJavaObject(paramObj, AppStatusVo.class);

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
