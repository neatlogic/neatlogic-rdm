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

package neatlogic.module.rdm.api.app;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListProjectAppApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "获取项目应用列表";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", desc = "项目id", isRequired = true, type = ApiParamType.LONG),
            @Param(name = "needIssueCount", desc = "是否需要返回任务数量", type = ApiParamType.INTEGER),
            @Param(name = "isMine", desc = "是否只查询包含当前用户任务的应用列表", type = ApiParamType.INTEGER)
    })
    @Output({@Param(explode = AppVo[].class)})
    @Description(desc = "获取项目应用列表接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("projectId");
        Integer needIssueCount = paramObj.getInteger("needIssueCount");
        Integer isMine = paramObj.getInteger("isMine");
        List<AppVo> appList = appMapper.getAppDetailByProjectId(projectId);

        if (needIssueCount != null && needIssueCount.equals(1)) {
            List<AppVo> userAppList = appMapper.getAppIssueCountByProjectIdAndUserId(projectId, (isMine != null && isMine.equals(1)) ? UserContext.get().getUserUuid(true) : null);
            Iterator<AppVo> itApp = appList.iterator();
            while (itApp.hasNext()) {
                AppVo appVo = itApp.next();
                Optional<AppVo> op = userAppList.stream().filter(ua -> ua.getId().equals(appVo.getId())).findFirst();
                if (!op.isPresent()) {
                    itApp.remove();
                } else {
                    appVo.setIssueCount(op.get().getIssueCount());
                }
            }
        }
        return appList;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/get";
    }
}
