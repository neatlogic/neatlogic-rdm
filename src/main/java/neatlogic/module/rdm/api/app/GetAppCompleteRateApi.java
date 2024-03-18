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

package neatlogic.module.rdm.api.app;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueCountVo;
import neatlogic.framework.rdm.dto.IssueVo;
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
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class GetAppCompleteRateApi extends PrivateApiComponentBase {
    @Resource
    private IssueMapper issueMapper;


    @Override
    public String getName() {
        return "nmraa.getappcompleterateapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "nmraa.getappapi.input.param.desc"),
            @Param(name = "fromId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.fromid"),
            @Param(name = "toId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.toid")})
    @Description(desc = "nmraa.getappcompleterateapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueVo issueVo = JSONObject.toJavaObject(paramObj, IssueVo.class);
        List<IssueCountVo> countList = issueMapper.getIssueCountByAppId(issueVo);
        float all = 0, end = 0;
        for (IssueCountVo count : countList) {
            if (count.getType().equals("end")) {
                end = count.getCount();
            } else if (count.getType().equals("all")) {
                all = count.getCount();
            }
        }
        if (all > 0) {
            return end / all;
        }
        return 0;
    }

    @Override
    public String getToken() {
        return "/rdm/app/complaterate";
    }
}
