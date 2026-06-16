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

package neatlogic.module.rdm.api.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.IssueRelVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.IssueRelDirection;
import neatlogic.framework.rdm.enums.IssueRelType;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.service.IssueRelStrategyService;
import neatlogic.module.rdm.service.IssueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveIssueRelApi extends PrivateApiComponentBase {
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private IssueService issueService;
    @Resource
    private IssueRelStrategyService issueRelStrategyService;

    @Override
    public String getName() {
        return "保存任务关联关系";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, isRequired = true, desc = "任务id，由direction参数决定是来源任务id还是目标任务id"),
            @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id，由direction参数决定是来源应用id还是目标应用id"),
            @Param(name = "direction", type = ApiParamType.ENUM, isRequired = true, member = IssueRelDirection.class, desc = "关系方向"),
            @Param(name = "idList", type = ApiParamType.JSONARRAY, isRequired = true, desc = "关联任务id列表，由direction参数决定是来源任务id还是目标任务id列表"),
            @Param(name = "relType", type = ApiParamType.ENUM, isRequired = true, member = IssueRelType.class, desc = "关系类型")
    })
    @Description(desc = "保存任务关联关系")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        IssueVo issueVo = issueMapper.getIssueById(id);
        Long appId = paramObj.getLong("appId");
        AppVo appVo = appMapper.getAppById(appId);
        String direction = paramObj.getString("direction");
        JSONArray idList = paramObj.getJSONArray("idList");
        String relType = paramObj.getString("relType");
        Long fromAppId = direction.equals(IssueRelDirection.FROM.getValue()) ? issueVo.getAppId() : appId;
        Long toAppId = direction.equals(IssueRelDirection.FROM.getValue()) ? appId : issueVo.getAppId();
        for (int i = 0; i < idList.size(); i++) {
            Long targetIssueId = idList.getLong(i);
            if (appVo != null && issueRelStrategyService.needCopy(fromAppId, toAppId, relType)) {
                Long existingCopyId = issueMapper.getRelIssueIdBySourceIssueId(id, relType, direction, appId, targetIssueId);
                if (existingCopyId != null) {
                    continue;
                }
                IssueVo copyIssue = issueService.copyIssue(targetIssueId);
                targetIssueId = copyIssue.getId();
            }
            IssueRelVo issueRelVo = buildIssueRel(issueVo, targetIssueId, appId, direction, relType);
            issueMapper.insertIssueRel(issueRelVo);
        }
        return null;
    }

    private IssueRelVo buildIssueRel(IssueVo issueVo, Long targetIssueId, Long appId, String direction, String relType) {
        IssueRelVo issueRelVo = new IssueRelVo();
        issueRelVo.setDirection(direction);
        issueRelVo.setRelType(relType);
        if (direction.equals(IssueRelDirection.FROM.getValue())) {
            issueRelVo.setFromIssueId(issueVo.getId());
            issueRelVo.setFromAppId(issueVo.getAppId());
            issueRelVo.setToIssueId(targetIssueId);
            issueRelVo.setToAppId(appId);
        } else {
            issueRelVo.setToIssueId(issueVo.getId());
            issueRelVo.setToAppId(issueVo.getAppId());
            issueRelVo.setFromIssueId(targetIssueId);
            issueRelVo.setFromAppId(appId);
        }
        return issueRelVo;
    }

    @Override
    public String getToken() {
        return "/rdm/issue/rel/save";
    }

}
