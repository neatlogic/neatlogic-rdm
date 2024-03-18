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

package neatlogic.module.rdm.api.project;

import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ProjectConditionVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "nmrap.searchprojectapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, desc = "common.keyword"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "common.currentpage"),
            @Param(name = "pageSize", type = ApiParamType.STRING, desc = "common.pagesize"),
            @Param(name = "isMine", type = ApiParamType.INTEGER, rule = "0,1", desc = "page.ismine"),
            @Param(name = "isClose", type = ApiParamType.INTEGER, rule = "0,1", desc = "page.isclose")})
    @Output({@Param(explode = BasePageVo.class)})
    @Description(desc = "nmrap.searchprojectapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        ProjectConditionVo projectVo = JSONObject.toJavaObject(paramObj, ProjectConditionVo.class);
        if (projectVo.getIsMine() != null && projectVo.getIsMine().equals(1)) {
            projectVo.setUserIdList(new ArrayList<String>() {{
                this.add(UserContext.get().getUserUuid(true));
            }});
        }
        List<Long> idList = projectMapper.searchProjectId(projectVo);
        List<ProjectVo> projectList = null;
        if (CollectionUtils.isNotEmpty(idList)) {
            projectVo.setIdList(idList);
            projectList = projectMapper.searchProject(projectVo);
            int rowNum = projectMapper.searchProjectCount(projectVo);
            projectVo.setRowNum(rowNum);
        }
        return TableResultUtil.getResult(projectList, projectVo);
    }

    @Override
    public String getToken() {
        return "/rdm/project/search";
    }
}
