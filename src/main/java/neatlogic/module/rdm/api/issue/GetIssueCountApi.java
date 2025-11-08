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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueCountVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetIssueCountApi extends PrivateApiComponentBase {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "获取任务数量";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "项目id"),
            @Param(name = "groupBy", type = ApiParamType.ENUM, rule = "day,month", isRequired = true, desc = "分组类型")})
    @Output({@Param(explode = IssueCountVo[].class)})
    @Description(desc = "获取任务数量")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueCountVo issueCountVo = JSON.toJavaObject(paramObj, IssueCountVo.class);
        return issueMapper.getIssueCountByProjectId(issueCountVo);
    }

    @Override
    public String getToken() {
        return "/rdm/project/issue/count";
    }
}
