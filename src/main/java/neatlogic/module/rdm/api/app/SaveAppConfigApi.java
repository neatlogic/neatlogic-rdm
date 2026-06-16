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

package neatlogic.module.rdm.api.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.exception.type.ParamIrregularException;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.enums.IssueRelType;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.service.IssueRelStrategyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveAppConfigApi extends PrivateApiComponentBase {
    @Resource
    private AppMapper appMapper;


    @Override
    public String getName() {
        return "nmraa.saveappconfigapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, isRequired = true, desc = "nmraa.getappapi.input.param.desc"),
            @Param(name = "config", type = ApiParamType.JSONOBJECT, isRequired = true, desc = "common.config")})
    @Description(desc = "nmraa.saveappconfigapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppVo appVo = JSON.toJavaObject(paramObj, AppVo.class);
        validateRelStrategy(appVo);
        appMapper.saveAppConfig(appVo);
        return null;
    }

    private void validateRelStrategy(AppVo appVo) {
        if (appVo == null || appVo.getId() == null || appVo.getConfig() == null) {
            return;
        }
        AppVo oldApp = appMapper.getAppById(appVo.getId());
        if (oldApp == null) {
            return;
        }
        JSONArray relStrategyList = appVo.getConfig().getJSONArray("relStrategyList");
        if (relStrategyList == null) {
            return;
        }
        for (int i = 0; i < relStrategyList.size(); i++) {
            JSONObject strategy = relStrategyList.getJSONObject(i);
            if (strategy == null) {
                throw new ParamIrregularException("relStrategyList");
            }
            String toAppType = strategy.getString("toAppType");
            String relType = strategy.getString("relType");
            String action = strategy.getString("action");
            if (appMapper.getAppByProjectIdAndType(oldApp.getProjectId(), toAppType) == null) {
                throw new ParamIrregularException("toAppType");
            }
            if (IssueRelType.getValue(relType) == null) {
                throw new ParamIrregularException("relType");
            }
            if (!IssueRelStrategyService.ACTION_ORIGINAL.equals(action) && !IssueRelStrategyService.ACTION_COPY.equals(action)) {
                throw new ParamIrregularException("action");
            }
        }
    }

    @Override
    public String getToken() {
        return "/rdm/app/config/save";
    }
}
