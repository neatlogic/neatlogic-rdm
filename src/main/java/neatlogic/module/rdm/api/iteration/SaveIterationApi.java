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
import neatlogic.framework.rdm.notify.constvalue.RdmIterationNotifyTriggerType;
import neatlogic.framework.rdm.notify.dto.RdmNotifyContextVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IterationMapper;
import neatlogic.module.rdm.notify.service.RdmNotifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveIterationApi extends PrivateApiComponentBase {
    @Resource
    private IterationMapper iterationMapper;

    @Resource
    private RdmNotifyService rdmNotifyService;


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
        IterationVo oldIterationVo = null;
        if (id != null) {
            oldIterationVo = iterationMapper.getIterationById(id);
        }
        IterationVo iterationVo = JSON.toJavaObject(paramObj, IterationVo.class);
        iterationVo.setStartDate(null);
        iterationVo.setEndDate(null);
        if (id == null) {
            iterationMapper.insertIteration(iterationVo);
        } else {
            iterationMapper.updateIteration(iterationVo);
        }
        IterationVo currentIterationVo = iterationMapper.getIterationById(iterationVo.getId());
        RdmNotifyContextVo contextVo = new RdmNotifyContextVo();
        contextVo.setBizType("iteration");
        contextVo.setIterationVo(currentIterationVo);
        contextVo.setOldIterationVo(oldIterationVo);
        if (oldIterationVo == null) {
            rdmNotifyService.notify(contextVo, RdmIterationNotifyTriggerType.CREATED);
        } else if (isChanged(oldIterationVo, currentIterationVo)) {
            rdmNotifyService.notify(contextVo, RdmIterationNotifyTriggerType.UPDATED);
        }
        return iterationVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/iteration/save";
    }

    /**
     * 迭代更新通知只关注可编辑的业务字段。
     */
    private boolean isChanged(IterationVo oldIterationVo, IterationVo currentIterationVo) {
        return !Objects.equals(oldIterationVo.getName(), currentIterationVo.getName())
                || !Objects.equals(oldIterationVo.getDescription(), currentIterationVo.getDescription())
                || !Objects.equals(oldIterationVo.getStartDate(), currentIterationVo.getStartDate())
                || !Objects.equals(oldIterationVo.getEndDate(), currentIterationVo.getEndDate());
    }

}
