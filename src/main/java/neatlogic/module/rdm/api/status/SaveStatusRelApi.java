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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppStatusRelVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveStatusRelApi extends PrivateApiComponentBase {

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "修改状态关系配置";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, isRequired = true, desc = "状态关系id"),
            @Param(name = "config", type = ApiParamType.JSONOBJECT, desc = "关系配置")
    })
    @Description(desc = "修改状态关系配置接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppStatusRelVo appStatusRelVo = JSONObject.toJavaObject(paramObj, AppStatusRelVo.class);
        appMapper.updateAppStatusRelConfig(appStatusRelVo);
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/statusrel/config/save";
    }
}
