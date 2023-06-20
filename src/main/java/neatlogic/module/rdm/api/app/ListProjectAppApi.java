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
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.IssueConditionVo;
import neatlogic.framework.rdm.enums.AppType;
import neatlogic.framework.rdm.enums.SystemAttrType;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListProjectAppApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "nmraa.listprojectappapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", desc = "term.rdm.projectid", isRequired = true, type = ApiParamType.LONG),
            @Param(name = "needIssueCount", desc = "nmraa.listprojectappapi.input.param.desc.needissuecount", type = ApiParamType.INTEGER),
            @Param(name = "isMine", desc = "nmraa.listprojectappapi.input.param.desc.ismine", type = ApiParamType.INTEGER),
            @Param(name = "isMyCreated", desc = "nmraa.listprojectappapi.input.param.desc.ismyreported", type = ApiParamType.INTEGER),
            @Param(name = "isEnd", type = ApiParamType.INTEGER, rule = "0,1", desc = "common.isend"),
            @Param(name = "isActive", type = ApiParamType.INTEGER, rule = "0,1", desc = "common.isactive"),
            @Param(name = "appType", type = ApiParamType.ENUM, member = AppType.class, desc = "term.rdm.apptype"),
            @Param(name = "needSystemAttr", desc = "nmraa.searchprivateattrapi.input.param.desc.needsystemattr", rule = "0,1", type = ApiParamType.INTEGER),
            @Param(name = "isFavorite", type = ApiParamType.INTEGER, rule = "0,1", desc = "nmrai.toggleissueisfavoriteapi.input.param.desc.isfavorite")
    })
    @Output({@Param(explode = AppVo[].class)})
    @Description(desc = "nmraa.listprojectappapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("projectId");
        Integer needIssueCount = paramObj.getInteger("needIssueCount");
        Integer isMine = paramObj.getInteger("isMine");
        Integer isMyCreated = paramObj.getInteger("isMyCreated");
        Integer isEnd = paramObj.getInteger("isEnd");
        Integer isFavorite = paramObj.getInteger("isFavorite");
        Integer isActive = paramObj.getInteger("isActive");
        Integer needSystemAttr = paramObj.getInteger("needSystemAttr");
        String appType = paramObj.getString("appType");
        List<AppVo> appList = appMapper.getAppDetailByProjectId(projectId, isActive);
        if (needSystemAttr != null && needSystemAttr.equals(1)) {
            for (AppVo appVo : appList) {
                List<AppAttrVo> systemAttrList = SystemAttrType.getSystemAttrList(appVo.getId());
                if (appVo.getAttrList() != null) {
                    appVo.getAttrList().addAll(0, systemAttrList);
                } else {
                    appVo.setAttrList(systemAttrList);
                }
            }
        }
        if (needIssueCount != null && needIssueCount.equals(1)) {
            IssueConditionVo issueConditionVo = new IssueConditionVo();
            issueConditionVo.setProjectId(projectId);
            issueConditionVo.setIsEnd(isEnd);
            issueConditionVo.setIsFavorite(isFavorite);
            issueConditionVo.setAppType(appType);
            if (isMine != null && isMine.equals(1)) {
                List<String> userIdList = new ArrayList<>();
                userIdList.add(UserContext.get().getUserUuid(true));
                issueConditionVo.setUserIdList(userIdList);
            }
            if (isMyCreated != null && isMyCreated.equals(1)) {
                issueConditionVo.setCreateUser(UserContext.get().getUserUuid(true));
            }
            List<AppVo> userAppList = issueMapper.getAppIssueCountByProjectIdAndUserId(issueConditionVo);
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
