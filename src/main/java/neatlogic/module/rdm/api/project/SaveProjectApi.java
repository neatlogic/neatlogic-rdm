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
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.ObjectType;
import neatlogic.framework.rdm.enums.PrivateAttr;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.*;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.service.ProjectService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectService projectService;

    @Override
    public String getName() {
        return "保存项目";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "id，不提供代表新增项目"),
            @Param(name = "name", type = ApiParamType.STRING, xss = true, isRequired = true, maxLength = 50, desc = "项目名称"),
            @Param(name = "templateId", type = ApiParamType.LONG, isRequired = true, desc = "项目类型"),
            @Param(name = "description", type = ApiParamType.STRING, desc = "说明", maxLength = 500, xss = true),
            @Param(name = "dateRange", type = ApiParamType.JSONARRAY, desc = "起止日期"),
            @Param(name = "memberIdList", type = ApiParamType.JSONARRAY, desc = "项目成员id列表"),
            @Param(name = "leaderIdList", type = ApiParamType.JSONARRAY, desc = "项目负责人id列表"),
            @Param(name = "color", type = ApiParamType.STRING, desc = "颜色标识")})
    @Output({@Param(name = "id", type = ApiParamType.STRING, desc = "模型id")})
    @Description(desc = "保存项目接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        ProjectVo projectVo = JSONObject.toJavaObject(paramObj, ProjectVo.class);
        if (projectMapper.checkProjectNameIsExists(projectVo) > 0) {
            throw new ProjectNameIsExistsException(projectVo.getName());
        }
        if (paramObj.getLong("id") == null) {
            ProjectTemplateVo projectTemplateVo = projectMapper.getProjectTemplateById(projectVo.getTemplateId());
            if (projectTemplateVo == null) {
                throw new ProjectTemplateNotFoundException(projectVo.getTemplateId());
            }
            projectVo.setType(projectTemplateVo.getName());
            projectMapper.insertProject(projectVo);
            List<ObjectVo> objectList = new ArrayList<>();
            for (ProjectTemplateObjectTypeVo objectType : projectTemplateVo.getObjectTypeList()) {
                ObjectVo objectVo = new ObjectVo();
                objectVo.setProjectId(projectVo.getId());
                objectVo.setType(objectType.getName());
                objectVo.setSort(objectType.getSort());
                projectMapper.insertObject(objectVo);

                PrivateAttr[] attrTypeList = ObjectType.getAttrList(objectType.getName());
                if (attrTypeList != null) {
                    int sort = 1;
                    for (PrivateAttr attrType : attrTypeList) {
                        ObjectAttrVo objectAttrVo = new ObjectAttrVo();
                        objectAttrVo.setName(attrType.getName());
                        objectAttrVo.setLabel(attrType.getLabel());
                        objectAttrVo.setType(attrType.getType());
                        objectAttrVo.setSort(sort);
                        objectAttrVo.setIsRequired(0);
                        objectAttrVo.setIsPrivate(1);
                        objectAttrVo.setIsActive(1);
                        objectAttrVo.setObjectId(objectVo.getId());
                        objectVo.addObjectAttr(objectAttrVo);
                        projectMapper.insertObjectAttr(objectAttrVo);
                        sort += 1;
                    }
                }
                objectList.add(objectVo);
            }
            for (ObjectVo objectVo : objectList) {
                EscapeTransactionJob.State s = projectService.buildObjectSchema(objectVo);
                if (!s.isSucceed()) {
                    throw new CreateObjectSchemaException(objectVo.getName());
                }
            }
        } else {
            ProjectVo checkProjectVo = projectMapper.getProjectById(id);
            if (checkProjectVo == null) {
                throw new ProjectNotFoundException(id);
            }
            String currentUserId = UserContext.get().getUserUuid(true);
            if (!checkProjectVo.getFcu().equals(currentUserId)) {
                if (CollectionUtils.isEmpty(checkProjectVo.getUserList()) || checkProjectVo.getUserList().stream().noneMatch(d -> d.getUserType().equals(ProjectUserType.LEADER.getValue()) && d.getUserId().equals(currentUserId))) {
                    throw new ProjectNotAuthException(projectVo.getName());
                }
            }
            projectMapper.updateProject(projectVo);
            //清除用户数据
            projectMapper.deleteProjectUserByProjectId(projectVo.getId());
        }
        if (CollectionUtils.isNotEmpty(projectVo.getMemberIdList())) {
            for (String userId : projectVo.getMemberIdList()) {
                ProjectUserVo projectUserVo = new ProjectUserVo();
                projectUserVo.setUserId(userId.replace("user#", ""));
                projectUserVo.setUserType(ProjectUserType.MEMBER.getValue());
                projectUserVo.setProjectId(projectVo.getId());
                projectMapper.insertProjectUser(projectUserVo);
            }
        }
        if (CollectionUtils.isNotEmpty(projectVo.getLeaderIdList())) {
            for (String userId : projectVo.getLeaderIdList()) {
                ProjectUserVo projectUserVo = new ProjectUserVo();
                projectUserVo.setUserId(userId.replace("user#", ""));
                projectUserVo.setUserType(ProjectUserType.LEADER.getValue());
                projectUserVo.setProjectId(projectVo.getId());
                projectMapper.insertProjectUser(projectUserVo);
            }
        }
        return projectVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/project/save";
    }
}
