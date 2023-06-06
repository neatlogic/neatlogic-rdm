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
            PriorityVo priorityVo = JSONObject.toJavaObject(list.getJSONObject(i), PriorityVo.class);
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
