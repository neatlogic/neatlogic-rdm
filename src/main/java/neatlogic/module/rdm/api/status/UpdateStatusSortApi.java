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

package neatlogic.module.rdm.api.status;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.PRIORITY_MANAGE;
import neatlogic.framework.rdm.dto.AppStatusVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = PRIORITY_MANAGE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class UpdateStatusSortApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "nmras.updatestatussortapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "statusIdList", type = ApiParamType.JSONARRAY, isRequired = true, desc = "common.statuslist")})
    @Description(desc = "nmras.updatestatussortapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        JSONArray list = paramObj.getJSONArray("statusIdList");
        for (int i = 0; i < list.size(); i++) {
            AppStatusVo appStatusVo = new AppStatusVo();
            appStatusVo.setId(list.getLong(i));
            appStatusVo.setSort(i + 1);
            appMapper.updateAppStatusSort(appStatusVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/status/updatesort";
    }
}
