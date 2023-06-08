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

package neatlogic.module.rdm.api.priority;

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
        PriorityVo priorityVo = JSONObject.toJavaObject(paramObj, PriorityVo.class);
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
