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

package neatlogic.module.rdm.api.object;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ObjectStatusVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.RegexUtils;
import neatlogic.module.rdm.dao.mapper.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveObjectStatusApi extends PrivateApiComponentBase {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public String getName() {
        return "保存对象状态";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "对象状态id，不提供代表添加"),
            @Param(name = "name", type = ApiParamType.REGEX, rule = RegexUtils.ENCHAR, isRequired = true, desc = "唯一标识"),
            @Param(name = "label", type = ApiParamType.STRING, isRequired = true, desc = "名称"),
            @Param(name = "objectId", type = ApiParamType.LONG, isRequired = true, desc = "对象id"),
            @Param(name = "description", type = ApiParamType.STRING, desc = "说明"),
            @Param(name = "color", type = ApiParamType.STRING, desc = "颜色")})
    @Output({@Param(explode = ObjectStatusVo.class)})
    @Description(desc = "保存对象状态接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        ObjectStatusVo objectStatusVo = JSONObject.toJavaObject(paramObj, ObjectStatusVo.class);

        if (paramObj.getLong("id") == null) {
            List<ObjectStatusVo> statusList = objectMapper.getStatusByObjectId(objectStatusVo.getObjectId());
            objectStatusVo.setSort(statusList.size() + 1);
            objectMapper.insertObjectStatus(objectStatusVo);
        } else {
            objectMapper.updateObjectStatus(objectStatusVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/object/status/save";
    }
}
