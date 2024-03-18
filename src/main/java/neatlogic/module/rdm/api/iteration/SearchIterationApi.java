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

package neatlogic.module.rdm.api.iteration;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IterationVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.IterationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchIterationApi extends PrivateApiComponentBase {

    @Resource
    private IterationMapper iterationMapper;

    @Override
    public String getName() {
        return "查询迭代";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, desc = "common.keyword"),
            @Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.projectid"),
            @Param(name = "isOpen", type = ApiParamType.INTEGER, rule = "0,1", desc = "term.rdm.isopen"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "common.currentpage"),
            @Param(name = "pageSize", type = ApiParamType.STRING, desc = "common.rownum")})
    @Output({@Param(explode = BasePageVo.class)})
    @Description(desc = "查询迭代接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IterationVo iterationVo = JSONObject.toJavaObject(paramObj, IterationVo.class);
        int rowNum = iterationMapper.searchIterationCount(iterationVo);
        List<IterationVo> iterationList = null;
        if (rowNum > 0) {
            iterationList = iterationMapper.searchIteration(iterationVo);
        }
        return TableResultUtil.getResult(iterationList, iterationVo);
    }

    @Override
    public String getToken() {
        return "/rdm/iteration/search";
    }
}
