/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.module.rdm.api.catalog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.lrcode.LRCodeManager;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppCatalogVo;
import neatlogic.framework.rdm.exception.CatalogNameIsExistsException;
import neatlogic.framework.rdm.exception.CatalogNotFoundException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.CatalogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveCatalogApi extends PrivateApiComponentBase {

    @Resource
    private CatalogMapper catalogMapper;

    @Override
    public String getName() {
        return "保存目录";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "目录id，不提供代表添加"), @Param(name = "name", type = ApiParamType.STRING, isRequired = true, desc = "目录名称"), @Param(name = "parentId", type = ApiParamType.LONG, desc = "父目录id"), @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id")})
    @Description(desc = "保存目录接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        AppCatalogVo appCatalogVo = JSON.toJavaObject(paramObj, AppCatalogVo.class);
        if (appCatalogVo.getParentId() != null) {
            if (catalogMapper.getAppCatalogById(appCatalogVo.getParentId()) == null) {
                throw new CatalogNotFoundException(appCatalogVo.getParentId());
            }
        }
        if (catalogMapper.checkAppCatalogNameIsExists(appCatalogVo) > 0) {
            throw new CatalogNameIsExistsException(appCatalogVo.getName());
        }
        if (id == null) {
            catalogMapper.insertAppCatalog(appCatalogVo);
        } else {
            catalogMapper.updateAppCatalog(appCatalogVo);
        }
        LRCodeManager.rebuildLeftRightCode("rdm_app_catalog", "id", "parent_id", "app_id = " + appCatalogVo.getAppId());
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/catalog/save";
    }
}
