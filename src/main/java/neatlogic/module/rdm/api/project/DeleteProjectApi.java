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

import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ObjectVo;
import neatlogic.framework.rdm.exception.CreateObjectSchemaException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.service.ProjectService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.DELETE)
@Transactional
public class DeleteProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;


    @Resource
    private ProjectService projectService;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "删除项目";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "项目id", isRequired = true, type = ApiParamType.LONG)})
    @Description(desc = "删除项目接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("id");
        List<ObjectVo> objectList = projectMapper.getObjectDetailByProjectId(projectId);
        for (ObjectVo objectVo : objectList) {
            issueMapper.deleteIssueByObjectId(objectVo.getId());
            projectMapper.deleteObjectById(objectVo.getId());
            EscapeTransactionJob.State s = projectService.dropObjectSchema(objectVo);
            if (!s.isSucceed()) {
                throw new CreateObjectSchemaException(objectVo.getName());
            }
        }
        projectMapper.deleteProjectById(projectId);
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/delete";
    }
}
