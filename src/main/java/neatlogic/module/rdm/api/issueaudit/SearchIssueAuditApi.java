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

package neatlogic.module.rdm.api.issueaudit;

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
    @Description(desc = "搜索变更历史接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueAuditVo issueAuditVo = JSONObject.toJavaObject(paramObj, IssueAuditVo.class);
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
