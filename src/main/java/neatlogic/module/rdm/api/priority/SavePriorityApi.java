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
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.PRIORITY_MANAGE;
import neatlogic.framework.rdm.dto.PriorityVo;
import neatlogic.framework.rdm.exception.PriorityNameIsExistsException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.PriorityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = PRIORITY_MANAGE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SavePriorityApi extends PrivateApiComponentBase {

    @Resource
    private PriorityMapper priorityMapper;

    @Override
    public String getName() {
        return "nmrap.savepriorityapi.description.desc";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "nmrap.savepriorityapi.input.param.desc.id")})
    @Description(desc = "nmrap.savepriorityapi.description.desc")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        PriorityVo priorityVo = JSON.toJavaObject(paramObj, PriorityVo.class);
        if (priorityMapper.checkPriorityNameIsExists(priorityVo) > 0) {
            throw new PriorityNameIsExistsException(priorityVo.getName());
        }
        if (id == null) {
            //默认放最后
            priorityVo.setSort(9999);
            priorityMapper.insertPriority(priorityVo);
        } else {
            priorityMapper.updatePriority(priorityVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/priority/save";
    }
}
