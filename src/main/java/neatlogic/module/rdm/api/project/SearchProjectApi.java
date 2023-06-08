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
            @Param(name = "isMine", type = ApiParamType.INTEGER, desc = "page.ismine"),
            @Param(name = "isClose", type = ApiParamType.INTEGER, desc = "page.isclose")})
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
