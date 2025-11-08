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

package neatlogic.module.rdm.api.issueaudit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueAuditVo;
import neatlogic.framework.rdm.dto.IssueCountVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.IssueAuditMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchIssueAuditApi extends PrivateApiComponentBase {

    @Resource
    private IssueAuditMapper issueAuditMapper;


    @Override
    public String getName() {
        return "搜索变更历史";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "issueId", type = ApiParamType.LONG, isRequired = true, desc = "任务id")
    })
    @Output({@Param(explode = IssueCountVo[].class)})
    @Description(desc = "搜索变更历史")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueAuditVo issueAuditVo = JSON.toJavaObject(paramObj, IssueAuditVo.class);
        int rowNum = issueAuditMapper.searchIssueAuditCount(issueAuditVo);
        issueAuditVo.setRowNum(rowNum);
        List<IssueAuditVo> issueAuditList = null;
        if (rowNum > 0) {
            issueAuditList = issueAuditMapper.searchIssueAudit(issueAuditVo);
        }
        return TableResultUtil.getResult(issueAuditList, issueAuditVo);
    }

    @Override
    public String getToken() {
        return "/rdm/issueaudit/search";
    }
}
