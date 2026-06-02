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
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.enums.IssueRelDirection;
import neatlogic.framework.rdm.enums.IssueRelType;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListRelIssueIdApi extends PrivateApiComponentBase {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "nmrai.listrelissueidapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "issueId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.issueid"),
            @Param(name = "relType", type = ApiParamType.ENUM, member = IssueRelType.class, isRequired = true, desc = "common.reltype"),
            @Param(name = "direction", type = ApiParamType.ENUM, member = IssueRelDirection.class, isRequired = true, desc = "term.rdm.reldirection"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc"),
    })
    @Output({@Param(explode = Long[].class)})
    @Description(desc = "nmrai.listrelissueidapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long issueId = paramObj.getLong("issueId");
        String relType = paramObj.getString("relType");
        String direction = paramObj.getString("direction");
        Long appId = paramObj.getLong("appId");
        return issueMapper.getRelIssueIdList(issueId, relType, direction, appId);
    }

    @Override
    public String getToken() {
        return "/rdm/issue/rel/list";
    }
}
