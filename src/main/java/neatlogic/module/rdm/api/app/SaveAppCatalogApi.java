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
import neatlogic.framework.lrcode.LRCodeManager;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppCatalogVo;
import neatlogic.framework.rdm.exception.AppCatalogNameIsExistsException;
import neatlogic.framework.rdm.exception.AppCatalogNotFoundException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveAppCatalogApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "保存应用目录";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "目录id，不提供代表添加"), @Param(name = "name", type = ApiParamType.STRING, isRequired = true, desc = "目录名称"), @Param(name = "parentId", type = ApiParamType.LONG, desc = "父目录id"), @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id")})
    @Description(desc = "保存应用目录接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        AppCatalogVo appCatalogVo = JSONObject.toJavaObject(paramObj, AppCatalogVo.class);
        if (appCatalogVo.getParentId() != null) {
            if (appMapper.getAppCatalogById(appCatalogVo.getParentId()) == null) {
                throw new AppCatalogNotFoundException(appCatalogVo.getParentId());
            }
        }
        if (appMapper.checkAppCatalogNameIsExists(appCatalogVo) > 0) {
            throw new AppCatalogNameIsExistsException(appCatalogVo.getName());
        }
        if (id == null) {
            appMapper.insertAppCatalog(appCatalogVo);
        } else {
            appMapper.updateAppCatalog(appCatalogVo);
        }
        LRCodeManager.rebuildLeftRightCode("rdm_app_catalog", "id", "parent_id", "app_id = " + appCatalogVo.getAppId());
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/catalog/save";
    }
}
