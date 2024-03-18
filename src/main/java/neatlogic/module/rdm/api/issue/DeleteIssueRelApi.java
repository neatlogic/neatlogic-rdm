/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
