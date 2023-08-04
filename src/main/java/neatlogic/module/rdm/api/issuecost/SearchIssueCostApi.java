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

package neatlogic.module.rdm.api.issuecost;

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
        IssueCostVo issueCostVo = JSONObject.toJavaObject(paramObj, IssueCostVo.class);
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
