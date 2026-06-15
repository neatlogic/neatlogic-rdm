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

package neatlogic.module.rdm.api.appattr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.enums.AppStatAttrType;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.exception.*;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.framework.util.RegexUtils;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.ProjectSchemaMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveAttrApi extends PrivateApiComponentBase {
    @Resource
    private AttrMapper attrMapper;
    @Resource
    private AppMapper appMapper;


    @Resource
    private ProjectSchemaMapper projectSchemaMapper;

    @Override
    public String getName() {
        return "nmraa.saveattrapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "nmraa.saveattrapi.input.param.desc.id"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc", isRequired = true),
            @Param(name = "name", type = ApiParamType.REGEX, rule = RegexUtils.ENCHAR, desc = "common.uniquename", isRequired = true, maxLength = 50),
            @Param(name = "label", type = ApiParamType.STRING, desc = "common.name", isRequired = true, maxLength = 50),
            @Param(name = "type", type = ApiParamType.ENUM, desc = "common.type", isRequired = true, member = AttrType.class),
            @Param(name = "statKey", type = ApiParamType.STRING, desc = "term.rdm.statkey", maxLength = 100),
            @Param(name = "isActive", type = ApiParamType.INTEGER, defaultValue = "0", desc = "common.isactive", rule = "1,0"),
            @Param(name = "isRequired", type = ApiParamType.INTEGER, defaultValue = "0", desc = "common.isrequired", rule = "1,0"),
            @Param(name = "description", type = ApiParamType.STRING, desc = "common.description", maxLength = 500)
    })
    @Output({@Param(name = "id", desc = "nmcaa.getattrapi.input.param.desc.id", type = ApiParamType.LONG)})
    @Description(desc = "nmraa.saveattrapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppAttrVo appAttrVo = JSON.toJavaObject(paramObj, AppAttrVo.class);
        appAttrVo.setIsPrivate(0);
        if (attrMapper.checkAttrNameIsExists(appAttrVo) > 0) {
            throw new AppAttrNameIsExistsException(appAttrVo.getName());
        }
        validateStatKey(appAttrVo);
        Long id = paramObj.getLong("id");
        if (id == null) {
            Integer maxSort = attrMapper.getMaxAppAttrSortByAppId(appAttrVo.getAppId());
            maxSort = maxSort == null ? 0 : maxSort;
            appAttrVo.setSort(maxSort + 1);
            attrMapper.insertAppAttr(appAttrVo);
            EscapeTransactionJob.State s = new EscapeTransactionJob(() -> projectSchemaMapper.insertAppTableAttr(appAttrVo.getTableName(), appAttrVo)).execute();
            if (!s.isSucceed()) {
                throw new InsertAttrToSchemaException(appAttrVo.getName());
            }
        } else {
            AppAttrVo oldAppAttrVo = attrMapper.getAttrById(id);
            if (oldAppAttrVo == null) {
                throw new AppAttrNotFoundException(id);
            }
            attrMapper.updateAppAttr(appAttrVo);
        }
        return appAttrVo.getId();
    }

    private void validateStatKey(AppAttrVo appAttrVo) {
        if (StringUtils.isBlank(appAttrVo.getStatKey())) {
            appAttrVo.setStatKey(null);
            return;
        }
        AppVo appVo = appMapper.getAppById(appAttrVo.getAppId());
        AppStatAttrType statAttrType = AppStatAttrType.get(appAttrVo.getStatKey());
        if (appVo == null || statAttrType == null || !statAttrType.getAppType().equalsIgnoreCase(appVo.getType())) {
            throw new AppAttrStatKeyInvalidException(appAttrVo.getStatKey());
        }
        if (!statAttrType.getAllowCustomAttr()) {
            throw new AppAttrStatKeyInvalidException(appAttrVo.getStatKey());
        }
        if (!statAttrType.isSupportAttrType(appAttrVo.getType())) {
            throw new AppAttrStatKeyInvalidException(appAttrVo.getStatKey());
        }
        if (attrMapper.checkAttrStatKeyIsExists(appAttrVo) > 0) {
            throw new AppAttrStatKeyDuplicateException(appAttrVo.getStatKey());
        }
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/attr/save";
    }
}
