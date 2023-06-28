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

package neatlogic.module.rdm.api.issue;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueRelVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueAuditMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.DELETE)
@Transactional
public class DeleteIssueRelApi extends PrivateApiComponentBase {
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private IssueAuditMapper issueAuditMapper;

    @Override
    public String getName() {
        return "nmrai.deleteissuerelapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "fromId", type = ApiParamType.LONG, isRequired = true, desc = "nmrai.searchissueapi.input.param.desc.fromid"),
            @Param(name = "toId", type = ApiParamType.LONG, isRequired = true, desc = "nmrai.searchissueapi.input.param.desc.toid")
    })
    @Description(desc = "nmrai.deleteissuerelapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long fromId = paramObj.getLong("fromId");
        Long toId = paramObj.getLong("toId");

        IssueRelVo issueRelVo = issueMapper.getIssueRel(fromId, toId);
        if (issueRelVo != null) {
            issueMapper.deleteIssueRel(issueRelVo);
/*
            IssueAuditVo fromIssueAuditVo = new IssueAuditVo();
            fromIssueAuditVo.setIssueId(fromId);
            fromIssueAuditVo.setRelAppType(issueRelVo.getToAppType());
            fromIssueAuditVo.setInputUser(UserContext.get().getUserUuid(true));
            fromIssueAuditVo.setOldValue(new JSONArray() {{
                this.add(new JSONObject() {{
                    this.put("id", issueRelVo.getToIssueId());
                    this.put("name", issueRelVo.getToIssueName());
                }});
            }});
            issueAuditMapper.insertIssueAudit(fromIssueAuditVo);

            IssueAuditVo toIssueAuditVo = new IssueAuditVo();
            toIssueAuditVo.setIssueId(toId);
            toIssueAuditVo.setRelAppType(issueRelVo.getFromAppType());
            toIssueAuditVo.setInputUser(UserContext.get().getUserUuid(true));
            toIssueAuditVo.setOldValue(new JSONArray() {{
                this.add(new JSONObject() {{
                    this.put("id", issueRelVo.getFromIssueId());
                    this.put("name", issueRelVo.getFromIssueName());
                }});
            }});
            issueAuditMapper.insertIssueAudit(toIssueAuditVo);*/
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/issue/rel/delete";
    }

}
