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
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.exception.type.ParamIrregularException;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueCopyRelVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.IssueNotAuthSearchException;
import neatlogic.framework.rdm.exception.IssueNotFoundException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.service.IssueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchIssueCopyApi extends PrivateApiComponentBase {
    @Resource
    private IssueService issueService;

    @Override
    public String getName() {
        return "查询issue副本";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "sourceIssueId", type = ApiParamType.LONG, isRequired = true, desc = "来源issue id")})
    @Output({@Param(name = "copyList", explode = IssueCopyRelVo[].class)})
    @Description(desc = "查询issue副本接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long sourceIssueId = paramObj.getLong("sourceIssueId");
        IssueVo sourceIssue = issueService.getIssueById(sourceIssueId);
        if (sourceIssue == null) {
            throw new IssueNotFoundException(sourceIssueId);
        }
        if (!ProjectAuthManager.checkProjectAuth(sourceIssue.getProjectId(), ProjectUserType.OWNER, ProjectUserType.LEADER, ProjectUserType.MEMBER)) {
            throw new IssueNotAuthSearchException();
        }
        if (sourceIssue.getSourceIssueId() != null) {
            throw new ParamIrregularException("sourceIssueId");
        }
        return issueService.searchIssueCopyRelBySourceIssueId(sourceIssueId);
    }

    @Override
    public String getToken() {
        return "/rdm/issue/copy/search";
    }
}
