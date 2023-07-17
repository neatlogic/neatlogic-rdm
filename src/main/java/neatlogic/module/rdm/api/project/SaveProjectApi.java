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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.rdm.exception.*;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.dao.mapper.ProjectTemplateMapper;
import neatlogic.module.rdm.service.ProjectService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveProjectApi extends PrivateApiComponentBase {
    @Resource
    private AttrMapper attrMapper;
    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectTemplateMapper projectTemplateMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private ProjectService projectService;

    @Override
    public String getName() {
        return "nmrap.saveprojectapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "nmrap.saveprojectapi.input.param.desc.id"),
            @Param(name = "name", type = ApiParamType.STRING, xss = true, isRequired = true, maxLength = 50, desc = "term.rdm.projectname"),
            @Param(name = "templateId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.projecttype"),
            @Param(name = "description", type = ApiParamType.STRING, desc = "common.description", maxLength = 500, xss = true),
            @Param(name = "dateRange", type = ApiParamType.JSONARRAY, desc = "term.rdm.startenddate"),
            @Param(name = "memberIdList", type = ApiParamType.JSONARRAY, desc = "nmrap.saveprojectapi.input.param.desc.memberidlist"),
            @Param(name = "leaderIdList", type = ApiParamType.JSONARRAY, desc = "term.rdm.project.manageridlist"),
            @Param(name = "color", type = ApiParamType.STRING, desc = "common.color")})
    @Output({@Param(name = "id", type = ApiParamType.STRING, desc = "term.cmdb.ciid")})
    @Description(desc = "nmrap.saveprojectapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        ProjectVo projectVo = JSONObject.toJavaObject(paramObj, ProjectVo.class);
        projectVo.setIsClose(0);
        if (projectMapper.checkProjectNameIsExists(projectVo) > 0) {
            throw new ProjectNameIsExistsException(projectVo.getName());
        }
        if (paramObj.getLong("id") == null) {
            ProjectTemplateVo projectTemplateVo = projectTemplateMapper.getProjectTemplateById(projectVo.getTemplateId());
            if (projectTemplateVo == null) {
                throw new ProjectTemplateNotFoundException(projectVo.getTemplateId());
            }
            projectVo.setType(projectTemplateVo.getName());
            projectMapper.insertProject(projectVo);
            List<AppVo> appList = new ArrayList<>();
            for (ProjectTemplateAppTypeVo appType : projectTemplateVo.getAppTypeList()) {
                AppVo appVo = new AppVo();
                appVo.setProjectId(projectVo.getId());
                appVo.setType(appType.getAppType());
                appVo.setSort(appType.getSort());
                appVo.setIsActive(1);
                appMapper.insertApp(appVo);
                AttrType[] attrTypeList = AppTypeManager.getAttrList(appType.getAppType());
                boolean hasAttr = false;
                if (appType.getConfig() != null) {
                    JSONArray attrObjList = appType.getConfig().getJSONArray("attrList");
                    if (CollectionUtils.isNotEmpty(attrObjList)) {
                        hasAttr = true;
                        for (int i = 0; i < attrObjList.size(); i++) {
                            JSONObject attrObj = attrObjList.getJSONObject(i);
                            AppAttrVo appAttrVo = JSONObject.toJavaObject(attrObj, AppAttrVo.class);
                            if (appAttrVo.getIsRequired() == null) {
                                appAttrVo.setIsRequired(0);
                            }
                            if (appAttrVo.getIsActive() == null) {
                                appAttrVo.setIsActive(0);
                            }
                            if (appAttrVo.getIsPrivate() == null) {
                                appAttrVo.setIsPrivate(0);
                            }
                            //如果是内置属性检查是否还存在
                            if (appAttrVo.getIsPrivate().equals(0) || (appAttrVo.getIsPrivate().equals(1) && AttrType.get(appAttrVo.getType()) != null)) {
                                appAttrVo.setId(null);
                                appAttrVo.setAppId(appVo.getId());
                                appVo.addAppAttr(appAttrVo);
                                attrMapper.insertAppAttr(appAttrVo);
                            }
                        }
                    }
                    JSONArray statusObjList = appType.getConfig().getJSONArray("statusList");
                    JSONArray statusRelObjList = appType.getConfig().getJSONArray("statusRelList");
                    List<AppStatusVo> statusList = new ArrayList<>();
                    List<AppStatusRelVo> statusRelList = new ArrayList<>();

                    if (CollectionUtils.isNotEmpty(statusObjList)) {
                        for (int i = 0; i < statusObjList.size(); i++) {
                            AppStatusVo statusVo = JSONObject.toJavaObject(statusObjList.getJSONObject(i), AppStatusVo.class);
                            statusList.add(statusVo);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(statusRelObjList)) {
                        for (int i = 0; i < statusRelObjList.size(); i++) {
                            AppStatusRelVo statusRelVo = JSONObject.toJavaObject(statusRelObjList.getJSONObject(i), AppStatusRelVo.class);
                            Optional<AppStatusVo> opFrom = statusList.stream().filter(d -> d.getId().equals(statusRelVo.getFromStatusId())).findFirst();
                            if (opFrom.isPresent()) {
                                AppStatusVo statusVo = opFrom.get();
                                statusVo.setId(null);
                                statusVo.setAppId(null);
                                statusRelVo.setFromStatusId(statusVo.getId());
                            } else {
                                continue;
                            }
                            Optional<AppStatusVo> toOp = statusList.stream().filter(d -> d.getId().equals(statusRelVo.getToStatusId())).findFirst();
                            if (toOp.isPresent()) {
                                AppStatusVo statusVo = toOp.get();
                                statusVo.setId(null);
                                statusVo.setAppId(null);
                                statusRelVo.setToStatusId(statusVo.getId());
                            } else {
                                continue;
                            }
                            statusRelVo.setId(null);
                            statusRelVo.setAppId(appVo.getId());
                            statusRelList.add(statusRelVo);
                        }
                    }
                    for (AppStatusVo status : statusList) {
                        if (status.getAppId() == null) {
                            status.setAppId(appVo.getId());
                            appMapper.insertAppStatus(status);
                        }
                    }
                    for (AppStatusRelVo statusRel : statusRelList) {
                        appMapper.insertAppStatusRel(statusRel);
                    }
                }
                if (!hasAttr) {
                    if (attrTypeList != null) {
                        int sort = 1;
                        for (AttrType attrType : attrTypeList) {
                            if (attrType.getBelong() == null || projectTemplateVo.getAppTypeList().stream().anyMatch(d -> d.getAppType().equalsIgnoreCase(attrType.getBelong()))) {
                                AppAttrVo appAttrVo = new AppAttrVo();
                                appAttrVo.setName(attrType.getName());
                                appAttrVo.setLabel(attrType.getLabel());
                                appAttrVo.setType(attrType.getType());
                                appAttrVo.setSort(sort);
                                appAttrVo.setIsRequired(0);
                                appAttrVo.setIsPrivate(1);
                                appAttrVo.setIsActive(1);
                                appAttrVo.setAppId(appVo.getId());
                                appVo.addAppAttr(appAttrVo);
                                attrMapper.insertAppAttr(appAttrVo);
                                sort += 1;
                            }
                        }
                    }
                }

                appList.add(appVo);
            }
            for (AppVo appVo : appList) {
                if (appVo.getHasIssue()) {
                    EscapeTransactionJob.State s = projectService.buildObjectSchema(appVo);
                    if (!s.isSucceed()) {
                        throw new CreateObjectSchemaException(appVo.getName());
                    }
                }
            }

            ProjectUserVo projectUserVo = new ProjectUserVo();
            projectUserVo.setUserId(UserContext.get().getUserUuid(true));
            projectUserVo.setUserType(ProjectUserType.OWNER.getValue());
            projectUserVo.setProjectId(projectVo.getId());
            projectMapper.insertProjectUser(projectUserVo);
        } else {
            ProjectVo checkProjectVo = projectMapper.getProjectById(id);
            if (checkProjectVo == null) {
                throw new ProjectNotFoundException(id);
            }
            //更新项目需要校验权限
            if (!checkProjectVo.getIsOwner() && !checkProjectVo.getIsLeader()) {
                throw new ProjectNotAuthException(checkProjectVo.getName());
            }
            String currentUserId = UserContext.get().getUserUuid(true);
            if (!checkProjectVo.getFcu().equals(currentUserId)) {
                if (CollectionUtils.isEmpty(checkProjectVo.getUserList()) || checkProjectVo.getUserList().stream().noneMatch(d -> d.getUserType().equals(ProjectUserType.LEADER.getValue()) && d.getUserId().equals(currentUserId))) {
                    throw new ProjectNotAuthException(projectVo.getName());
                }
            }
            projectMapper.updateProject(projectVo);
            //清除用户数据
            projectMapper.deleteProjectUserByProjectId(projectVo.getId(), new ArrayList<String>() {{
                this.add(ProjectUserType.OWNER.getValue());
                this.add(ProjectUserType.MEMBER.getValue());
                this.add(ProjectUserType.LEADER.getValue());
            }});
        }
        if (CollectionUtils.isNotEmpty(projectVo.getUserList())) {
            for (ProjectUserVo userVo : projectVo.getUserList()) {
                for (ProjectUserTypeVo userTypeVo : userVo.getUserTypeList()) {
                    ProjectUserVo projectUserVo = new ProjectUserVo();
                    projectUserVo.setUserId(userVo.getUserId().replace(GroupSearch.USER.getValuePlugin(), ""));
                    projectUserVo.setUserType(userTypeVo.getUserType());
                    projectUserVo.setProjectId(projectVo.getId());
                    projectMapper.insertProjectUser(projectUserVo);
                }
            }
        }

        return projectVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/project/save";
    }
}
