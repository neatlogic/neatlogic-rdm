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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppStatusVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListStatusApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "nmras.liststatusapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "nmraa.getappapi.input.param.desc"),
            @Param(name = "status", type = ApiParamType.LONG, desc = "nmras.liststatusapi.input.param.desc.status"),
            @Param(name = "needIssueCount", type = ApiParamType.INTEGER, rule = "0,1", desc = "nmras.liststatusapi.input.param.desc.needissuecount"),
            @Param(name = "fromId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.fromid"),
            @Param(name = "toId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.toid")})
    @Output({@Param(explode = AppStatusVo[].class)})
    @Description(desc = "nmras.liststatusapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueVo issueVo = JSONObject.toJavaObject(paramObj, IssueVo.class);
        Long status = paramObj.getLong("status");
        if (status == null) {
            issueVo.setStatus(0L);
        }
        /*AppStatusVo startStatus = null;
        if (status != null && status.equals(0L)) {
            List<AppStatusVo> statusList = appMapper.getStatusByAppId(issueVo);
            Optional<AppStatusVo> op = statusList.stream().filter(d -> d.getIsStart() != null && d.getIsStart().equals(1)).findFirst();
            if (op.isPresent()) {
                startStatus = op.get();
                status = startStatus.getId();
            } else {
                status = null;
            }
            issueVo.setStatus(status);
        }*/
        List<AppStatusVo> statusList = appMapper.getStatusByAppId(issueVo);

        for (int s = statusList.size() - 1; s >= 0; s--) {
            AppStatusVo appStatus = statusList.get(s);
            if (appStatus.getConfig() != null && CollectionUtils.isNotEmpty(appStatus.getConfig().getJSONArray("authList"))) {
                boolean hasAuth = false;
                JSONArray authList = appStatus.getConfig().getJSONArray("authList");
                for (int i = 0; i < authList.size(); i++) {
                    JSONObject authObj = authList.getJSONObject(i);
                    String value = authObj.getString("value");
                    if (value.startsWith("user#")) {
                        if (UserContext.get().getAuthenticationInfoVo().getUserUuid().equalsIgnoreCase(value.replace("user#", ""))) {
                            hasAuth = true;
                            break;
                        }
                    } else if (value.startsWith("role#")) {
                        if (UserContext.get().getAuthenticationInfoVo().getRoleUuidList().contains(value.replace("role#", ""))) {
                            hasAuth = true;
                            break;
                        }
                    } else if (value.startsWith("team#")) {
                        if (UserContext.get().getAuthenticationInfoVo().getTeamUuidList().contains(value.replace("team#", ""))) {
                            hasAuth = true;
                            break;
                        }
                    }
                }
                if (!hasAuth) {
                    statusList.remove(s);
                }
            }
        }
        /*if (startStatus != null && !statusList.contains(startStatus)) {
            statusList.add(0, startStatus);
        }*/
        return statusList;
    }

    @Override
    public String getToken() {
        return "/rdm/status/list";
    }
}
