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

package neatlogic.module.rdm.api.priority;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.PRIORITY_MANAGE;
import neatlogic.framework.rdm.dto.PriorityVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.$;
import neatlogic.module.rdm.dao.mapper.PriorityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = PRIORITY_MANAGE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class UpdatePrioritySortApi extends PrivateApiComponentBase {

    @Resource
    private PriorityMapper priorityMapper;

    @Override
    public String getName() {
        return $.t("nmrap.updateprioritysortapi.getname");
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "priorityList", type = ApiParamType.JSONARRAY, isRequired = true, desc = "nmrap.updateprioritysortapi.input.param.desc.prioritylist")})
    @Description(desc = "nmrap.updateprioritysortapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        JSONArray list = paramObj.getJSONArray("priorityList");
        for (int i = 0; i < list.size(); i++) {
            PriorityVo priorityVo = JSON.toJavaObject(list.getJSONObject(i), PriorityVo.class);
            priorityVo.setSort(i + 1);
            priorityMapper.updatePrioritySort(priorityVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/priority/updatesort";
    }
}
