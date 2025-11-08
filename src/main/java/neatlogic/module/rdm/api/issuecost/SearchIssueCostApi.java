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

package neatlogic.module.rdm.api.issuecost;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueCostVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.IssueCostNotAuthSearchException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.IssueCostMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchIssueCostApi extends PrivateApiComponentBase {
    @Resource
    private IssueCostMapper issueCostMapper;

    @Override
    public String getName() {
        return "nmrai.searchissuecostapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "issueId", type = ApiParamType.LONG, desc = "term.rdm.issueid", isRequired = true),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "common.currentpage"),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "common.pagesize")})
    @Output({
            @Param(explode = BasePageVo.class),
            @Param(name = "tbodyList", explode = IssueVo[].class)})
    @Description(desc = "nmrai.searchissueapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long issueId = paramObj.getLong("issueId");
        if (!ProjectAuthManager.checkIssueAuth(issueId, ProjectUserType.OWNER, ProjectUserType.LEADER, ProjectUserType.MEMBER)) {
            throw new IssueCostNotAuthSearchException();
        }
        IssueCostVo issueCostVo = JSON.toJavaObject(paramObj, IssueCostVo.class);
        int rowNum = issueCostMapper.searchIssueCostCount(issueCostVo);
        List<IssueCostVo> issueCostList = null;
        if (rowNum > 0) {
            issueCostList = issueCostMapper.searchIssueCost(issueCostVo);
        }
        return TableResultUtil.getResult(issueCostList, issueCostVo);
    }

    @Override
    public String getToken() {
        return "/rdm/issuecost/search";
    }
}
