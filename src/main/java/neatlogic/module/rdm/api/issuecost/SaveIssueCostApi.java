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
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueCostVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.IssueCostNotAuthSaveException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.IssueCostMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveIssueCostApi extends PrivateApiComponentBase {
    @Resource
    private IssueCostMapper issueCostMapper;


    @Override
    public String getName() {
        return "nmrai.saveissuecostapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "id", type = ApiParamType.LONG, desc = "id"),
            @Param(name = "issueId", type = ApiParamType.LONG, desc = "term.rdm.issueid", isRequired = true),
            @Param(name = "costDate", type = ApiParamType.STRING, desc = "term.rdm.costdate", isRequired = true),
            @Param(name = "timecost", type = ApiParamType.INTEGER, desc = "term.rdm.costtime", isRequired = true),
            @Param(name = "description", type = ApiParamType.STRING, xss = true, desc = "common.description")})
    @Description(desc = "nmrai.saveissuecostapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        Long issueId = paramObj.getLong("issueId");
        if (!ProjectAuthManager.checkIssueAuth(issueId, ProjectUserType.OWNER, ProjectUserType.LEADER, ProjectUserType.MEMBER)) {
            throw new IssueCostNotAuthSaveException();
        }
        IssueCostVo issueCostVo = JSONObject.toJavaObject(paramObj, IssueCostVo.class);
        if (id != null) {
            issueCostMapper.updateIssueCost(issueCostVo);
        } else {
            issueCostMapper.insertIssueCost(issueCostVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/issuecost/save";
    }
}
