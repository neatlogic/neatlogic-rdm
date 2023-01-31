/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.project;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.dto.*;
import codedriver.framework.rdm.enums.ObjectType;
import codedriver.framework.rdm.enums.PrivateAttr;
import codedriver.framework.rdm.enums.ProjectUserType;
import codedriver.framework.rdm.exception.*;
import codedriver.framework.restful.annotation.*;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.framework.transaction.core.EscapeTransactionJob;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.service.ProjectService;
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
