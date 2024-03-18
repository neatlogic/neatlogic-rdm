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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class CheckIssueIsFavoriteApi extends PrivateApiComponentBase {


    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "nmrai.checkissueisfavoriteapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "term.rdm.issueid")})
    @Output({@Param(type = ApiParamType.INTEGER, name = "Return", desc = "nmrai.checkissueisfavoriteapi.output.param.desc")})
    @Description(desc = "nmrai.checkissueisfavoriteapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        return issueMapper.checkIssueIsFavorite(paramObj.getLong("id"), UserContext.get().getUserUuid(true));
    }

    @Override
    public String getToken() {
        return "/rdm/issue/favorite/check";
    }
}
