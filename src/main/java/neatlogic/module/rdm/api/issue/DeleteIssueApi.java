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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerFactory;
import neatlogic.framework.fulltextindex.core.IFullTextIndexHandler;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.enums.IssueFullTextIndexType;
import neatlogic.framework.rdm.exception.IssueNotDeleteAuthException;
import neatlogic.framework.rdm.exception.IssueNotFoundException;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.DELETE)
@Transactional
public class DeleteIssueApi extends PrivateApiComponentBase {

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "nmrai.deleteissueapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "term.rdm.issueid", isRequired = true, type = ApiParamType.LONG)})
    @Description(desc = "nmrai.deleteissueapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long issueId = paramObj.getLong("id");
        IssueVo issueVo = issueMapper.getIssueById(issueId);
        if (issueVo == null) {
            throw new IssueNotFoundException(issueId);
        }
        ProjectVo projectVo = projectMapper.getProjectById(issueVo.getProjectId());
        if (projectVo == null) {
            throw new ProjectNotFoundException(issueVo.getProjectId());
        }
        if (projectVo.getIsLeader() || projectVo.getIsOwner() || projectVo.getIsMember() && issueVo.getCreateUser().equalsIgnoreCase(UserContext.get().getUserUuid(true))) {
            issueMapper.deleteIssueById(issueVo);
            IFullTextIndexHandler indexHandler = FullTextIndexHandlerFactory.getHandler(IssueFullTextIndexType.ISSUE);
            if (indexHandler != null) {
                indexHandler.deleteIndex(issueVo.getId());
            }
        } else {
            throw new IssueNotDeleteAuthException();
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/issue/delete";
    }
}
