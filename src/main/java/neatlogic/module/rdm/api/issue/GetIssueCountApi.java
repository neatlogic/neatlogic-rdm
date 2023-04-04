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
import neatlogic.framework.rdm.dto.IssueCountVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetIssueCountApi extends PrivateApiComponentBase {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "获取任务数量";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "项目id"),
            @Param(name = "groupBy", type = ApiParamType.ENUM, rule = "day,month", isRequired = true, desc = "分组类型")})
    @Output({@Param(explode = IssueCountVo[].class)})
    @Description(desc = "获取任务数量接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueCountVo issueCountVo = JSONObject.toJavaObject(paramObj, IssueCountVo.class);
        return issueMapper.getIssueCountByProjectId(issueCountVo);
    }

    @Override
    public String getToken() {
        return "/rdm/project/issue/count";
    }
}
