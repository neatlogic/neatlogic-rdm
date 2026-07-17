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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerFactory;
import neatlogic.framework.fulltextindex.core.IFullTextIndexHandler;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.enums.IssueFullTextIndexType;
import neatlogic.framework.rdm.exception.IssueNotDeleteAuthException;
import neatlogic.framework.rdm.exception.IssueNotFoundException;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.framework.rdm.notify.constvalue.RdmIssueNotifyTriggerType;
import neatlogic.framework.rdm.notify.dto.RdmNotifyContextVo;
import neatlogic.framework.notify.dto.InvokeNotifyPolicyConfigVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.notify.service.RdmNotifyService;
import neatlogic.module.rdm.service.IssueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.DELETE)
@Transactional
public class DeleteIssueApi extends PrivateApiComponentBase {

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private RdmNotifyService rdmNotifyService;

    @Resource
    private IssueService issueService;

    @Override
    public String getName() {
        return "nmrai.deleteissueapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "term.rdm.issueid", isRequired = true, type = ApiParamType.LONG)})
    @Description(desc = "nmrai.deleteissueapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long issueId = paramObj.getLong("id");
        // 删除前读取完整Issue及自定义属性，供事务提交后的通知参数映射使用。
        IssueVo issueVo = issueService.getIssueById(issueId);
        if (issueVo == null) {
            throw new IssueNotFoundException(issueId);
        }
        ProjectVo projectVo = projectMapper.getProjectById(issueVo.getProjectId());
        if (projectVo == null) {
            throw new ProjectNotFoundException(issueVo.getProjectId());
        }
        if (projectVo.getIsLeader() || projectVo.getIsOwner() || projectVo.getIsMember() && issueVo.getCreateUser().equalsIgnoreCase(UserContext.get().getUserUuid(true))) {
            neatlogic.framework.rdm.dto.AppVo appVo = appMapper.getAppById(issueVo.getAppId());
            RdmNotifyContextVo notifyContextVo = new RdmNotifyContextVo();
            notifyContextVo.setBizType(appVo.getType());
            notifyContextVo.setProjectVo(projectVo);
            notifyContextVo.setAppVo(appVo);
            notifyContextVo.setIssueVo(issueVo);
            notifyContextVo.setNotifyPolicyConfig(appVo.getConfig().getObject("notifyPolicyConfig", InvokeNotifyPolicyConfigVo.class));
            issueMapper.deleteIssueById(issueVo);
            IFullTextIndexHandler indexHandler = FullTextIndexHandlerFactory.getHandler(IssueFullTextIndexType.ISSUE);
            if (indexHandler != null) {
                indexHandler.deleteIndex(issueVo.getId());
            }
            rdmNotifyService.notify(notifyContextVo, RdmIssueNotifyTriggerType.DELETED);
        } else {
            throw new IssueNotDeleteAuthException();
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/issue/delete";
    }
}
