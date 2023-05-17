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
public class SaveIterationApi extends PrivateApiComponentBase {
    @Resource
    private IterationMapper iterationMapper;


    @Override
    public String getName() {
        return "保存迭代";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "id，不提供代表新增迭代"),
            @Param(name = "name", type = ApiParamType.STRING, desc = "名称", xss = true, maxLength = 100, isRequired = true),
            @Param(name = "dateRange", type = ApiParamType.JSONARRAY, desc = "时间", isRequired = true),
            @Param(name = "description", type = ApiParamType.STRING, desc = "说明", maxLength = 1000)
    })
    @Output({@Param(name = "id", type = ApiParamType.LONG, desc = "迭代id")})
    @Description(desc = "保存迭代接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        IterationVo iterationVo = JSONObject.toJavaObject(paramObj, IterationVo.class);
        if (id == null) {
            iterationMapper.insertIteration(iterationVo);
        } else {
            iterationMapper.updateIteration(iterationVo);
        }
        return iterationVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/iteration/save";
    }

}
