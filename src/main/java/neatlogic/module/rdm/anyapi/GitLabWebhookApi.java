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

package neatlogic.module.rdm.anyapi;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.constvalue.ApiAnonymousAccessSupportEnum;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.publicapi.PublicApiComponentBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class GitLabWebhookApi extends PublicApiComponentBase {


    @Override
    public String getName() {
        return "Gitlab Webhook Api";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public ApiAnonymousAccessSupportEnum supportAnonymousAccess() {
        return ApiAnonymousAccessSupportEnum.ANONYMOUS_ACCESS_WITHOUT_ENCRYPTION;
    }

    @Description(desc = "Gitlab Webhook Api")
    @Override
    public Object myDoService(JSONObject paramObj) {
        return paramObj.getString("token");
    }

    @Override
    public String getToken() {
        return "/rdm/webhook/gitlab";
    }
}
