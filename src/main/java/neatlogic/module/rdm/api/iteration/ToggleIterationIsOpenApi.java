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

package neatlogic.module.rdm.api.iteration;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IterationVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IterationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class ToggleIterationIsOpenApi extends PrivateApiComponentBase {
    @Resource
    private IterationMapper iterationMapper;


    @Override
    public String getName() {
        return "修改迭代开关状态";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "id", isRequired = true),
            @Param(name = "isOpen", type = ApiParamType.INTEGER, desc = "是否开启", isRequired = true)
    })
    @Description(desc = "修改迭代开关状态接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IterationVo iterationVo = JSONObject.toJavaObject(paramObj, IterationVo.class);
        iterationMapper.updateIterationIsOpen(iterationVo);
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/iteration/isopen/toggle";
    }

}
