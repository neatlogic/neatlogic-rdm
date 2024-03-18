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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueRelVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.IssueRelDirection;
import neatlogic.framework.rdm.enums.IssueRelType;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveIssueRelApi extends PrivateApiComponentBase {
    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "保存任务关联关系";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, isRequired = true, desc = "任务id，由direction参数决定是来源任务id还是目标任务id"),
            @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id，由direction参数决定是来源应用id还是目标应用id"),
            @Param(name = "direction", type = ApiParamType.ENUM, isRequired = true, member = IssueRelDirection.class, desc = "关系方向"),
            @Param(name = "idList", type = ApiParamType.JSONARRAY, isRequired = true, desc = "关联任务id列表，由direction参数决定是来源任务id还是目标任务id列表"),
            @Param(name = "relType", type = ApiParamType.ENUM, isRequired = true, member = IssueRelType.class, desc = "关系类型")
    })
    @Description(desc = "保存任务关联关系接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        IssueVo issueVo = issueMapper.getIssueById(id);
        Long appId = paramObj.getLong("appId");
        String direction = paramObj.getString("direction");
        JSONArray idList = paramObj.getJSONArray("idList");
        String relType = paramObj.getString("relType");
        for (int i = 0; i < idList.size(); i++) {
            IssueRelVo issueRelVo = new IssueRelVo();
            issueRelVo.setDirection(direction);
            issueRelVo.setRelType(relType);
            if (direction.equals(IssueRelDirection.FROM.getValue())) {
                issueRelVo.setFromIssueId(id);
                issueRelVo.setFromAppId(issueVo.getAppId());
                issueRelVo.setToIssueId(idList.getLong(i));
                issueRelVo.setToAppId(appId);
            } else {
                issueRelVo.setToIssueId(id);
                issueRelVo.setToAppId(issueVo.getAppId());
                issueRelVo.setFromIssueId(idList.getLong(i));
                issueRelVo.setFromAppId(appId);
            }
            issueMapper.insertIssueRel(issueRelVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/issue/rel/save";
    }

}
