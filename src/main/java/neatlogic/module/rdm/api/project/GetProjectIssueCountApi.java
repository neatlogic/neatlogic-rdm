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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueConditionVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetProjectIssueCountApi extends PrivateApiComponentBase {

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "获取项目任务数量";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "isMine", desc = "nmraa.listprojectappapi.input.param.desc.ismine", type = ApiParamType.INTEGER),
            @Param(name = "isMyCreated", desc = "nmraa.listprojectappapi.input.param.desc.ismyreported", type = ApiParamType.INTEGER),
            @Param(name = "isEnd", type = ApiParamType.INTEGER, rule = "0,1", desc = "common.isend"),
            @Param(name = "isProcessed", type = ApiParamType.INTEGER, rule = "0,1", desc = "term.rdm.isprocessed"),
            @Param(name = "isFavorite", type = ApiParamType.INTEGER, rule = "0,1", desc = "nmrai.toggleissueisfavoriteapi.input.param.desc.isfavorite")})
    @Output({@Param(explode = ProjectVo[].class)})
    @Description(desc = "获取项目任务数量")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Integer isMine = paramObj.getInteger("isMine");
        Integer isMyCreated = paramObj.getInteger("isMyCreated");
        Integer isEnd = paramObj.getInteger("isEnd");
        Integer isFavorite = paramObj.getInteger("isFavorite");
        Integer isProcessed = paramObj.getInteger("isProcessed");
        IssueConditionVo issueConditionVo = new IssueConditionVo();
        issueConditionVo.setIsEnd(isEnd);
        issueConditionVo.setIsFavorite(isFavorite);
        issueConditionVo.setIsProcessed(isProcessed);
        if ((isMine != null && isMine.equals(1))) {
            List<String> userIdList = new ArrayList<>();
            userIdList.add(UserContext.get().getUserUuid(true));
            issueConditionVo.setUserIdList(userIdList);
        }
        if (isMyCreated != null && isMyCreated.equals(1)) {
            issueConditionVo.setCreateUser(UserContext.get().getUserUuid(true));
        }
        return issueMapper.getProjectIssueCountByUserId(issueConditionVo);
    }

    @Override
    public String getToken() {
        return "/rdm/project/issuecount/get";
    }
}
