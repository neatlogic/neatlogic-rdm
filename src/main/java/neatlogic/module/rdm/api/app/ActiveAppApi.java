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
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.rdm.enums.core.IAppType;
import neatlogic.framework.rdm.exception.CreateObjectSchemaException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class ActiveAppApi extends PrivateApiComponentBase {
    @Resource
    private AttrMapper attrMapper;


    @Resource
    private AppMapper appMapper;

    @Resource
    private ProjectService projectService;


    @Override
    public String getName() {
        return "激活应用";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.projectid"),
            @Param(name = "appType", type = ApiParamType.STRING, isRequired = true, desc = "term.rdm.apptype")})
    @Description(desc = "激活应用")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("projectId");
        String appType = paramObj.getString("appType");
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
                if (appVo.getHasIssue()) {
                    EscapeTransactionJob.State s = projectService.buildObjectSchema(appVo);
                    if (!s.isSucceed()) {
                        throw new CreateObjectSchemaException(appVo.getName());
                    }
                }
            }
        } else {
            if (appVo.getIsActive().equals(0)) {
                appMapper.updateAppIsActive(appVo.getId(), 1);
            }
        }

        return appVo != null ? appVo.getId() : null;
    }

    @Override
    public String getToken() {
        return "/rdm/app/active";
    }
}
