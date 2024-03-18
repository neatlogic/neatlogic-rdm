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
