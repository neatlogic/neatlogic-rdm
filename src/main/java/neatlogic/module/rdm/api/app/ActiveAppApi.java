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

package neatlogic.module.rdm.api.app;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.rdm.enums.core.IAppType;
import neatlogic.framework.rdm.exception.CreateObjectSchemaException;
import neatlogic.framework.rdm.exception.ProjectNotAuthException;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.service.ProjectService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class ActiveAppApi extends PrivateApiComponentBase {
    @Resource
    private AttrMapper attrMapper;

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private AppMapper appMapper;

    @Resource
    private ProjectService projectService;


    @Override
    public String getName() {
        return "nmraa.activeappapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.projectid"),
            @Param(name = "appType", type = ApiParamType.STRING, isRequired = true, desc = "term.rdm.apptype")})
    @Description(desc = "nmraa.activeappapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("projectId");
        String appType = paramObj.getString("appType");
        ProjectVo projectVo = projectMapper.getProjectById(projectId);
        if (projectVo == null) {
            throw new ProjectNotFoundException(projectId);
        }
        if (!projectVo.getIsLeader() && !projectVo.getIsOwner()) {
            throw new ProjectNotAuthException(projectVo.getName());
        }
        AppVo appVo = appMapper.getAppByProjectIdAndType(projectId, appType);
        if (appVo == null) {
            IAppType aType = AppTypeManager.get(appType);
            if (aType != null) {
                appVo = new AppVo();
                appVo.setProjectId(projectId);
                appVo.setType(aType.getName());
                appVo.setSort(999);
                appVo.setIsActive(1);
                appMapper.insertApp(appVo);
                AttrType[] attrTypeList = AppTypeManager.getAttrList(aType.getName());
                if (attrTypeList != null) {
                    int sort = 1;
                    for (AttrType attrType : attrTypeList) {
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
        } else {
            if (appVo.getIsActive().equals(0)) {
                appMapper.updateAppIsActive(appVo.getId(), 1);
                List<AppAttrVo> oldAttrList = attrMapper.getAttrByAppId(appVo.getId());
                AttrType[] attrTypeList = AppTypeManager.getAttrList(appVo.getType());
                if (CollectionUtils.isNotEmpty(oldAttrList)) {
                    for (AppAttrVo attr : oldAttrList) {
                        if (attr.getIsPrivate().equals(1) && (attrTypeList == null || Arrays.stream(attrTypeList).noneMatch(d -> d.getType().equals(attr.getType())))) {
                            attrMapper.deleteAppAttrById(attr.getId());
                        }
                    }
                }
                if (attrTypeList != null) {
                    int sort = 1;
                    for (AttrType attrType : attrTypeList) {
                        if (CollectionUtils.isEmpty(oldAttrList) || oldAttrList.stream().noneMatch(d -> d.getType().equals(attrType.getType()))) {
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
        }
        if (appVo != null && appVo.getHasIssue()) {
            EscapeTransactionJob.State s = projectService.buildObjectSchema(appVo);
            if (!s.isSucceed()) {
                throw new CreateObjectSchemaException(appVo.getName());
            }
        }
        return appVo != null ? appVo.getId() : null;
    }

    @Override
    public String getToken() {
        return "/rdm/app/active";
    }
}
