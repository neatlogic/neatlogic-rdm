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

package neatlogic.module.rdm.api.iteration;

import com.alibaba.fastjson.JSON;
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
            @Param(name = "projectId", type = ApiParamType.LONG, desc = "项目id", isRequired = true),
            @Param(name = "dateRange", type = ApiParamType.JSONARRAY, desc = "时间", isRequired = true),
            @Param(name = "description", type = ApiParamType.STRING, desc = "说明", maxLength = 1000)
    })
    @Output({@Param(name = "id", type = ApiParamType.LONG, desc = "迭代id")})
    @Description(desc = "保存迭代接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        IterationVo iterationVo = JSON.toJavaObject(paramObj, IterationVo.class);
        iterationVo.setStartDate(null);
        iterationVo.setEndDate(null);
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
