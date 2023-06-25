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
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.WebhookConfigVo;
import neatlogic.framework.rdm.dto.WebhookDataVo;
import neatlogic.framework.rdm.webhook.IWebhookHandler;
import neatlogic.framework.rdm.webhook.WebhookFactory;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.ApiAnonymousAccessSupportEnum;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.WebhookMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveWebhookDataApi extends PrivateApiComponentBase {

    @Resource
    private WebhookMapper webhookMapper;

    @Resource
    private AppMapper appMapper;


    @Override
    public String getName() {
        return "nmra.savewebhookdataapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public ApiAnonymousAccessSupportEnum supportAnonymousAccess() {
        return ApiAnonymousAccessSupportEnum.ANONYMOUS_ACCESS_WITHOUT_ENCRYPTION;
    }

    @Input({@Param(name = "_t", type = ApiParamType.STRING, isRequired = true, desc = "nmra.savewebhookdataapi.input.param.desc")})
    @Output({@Param})
    @Description(desc = "nmra.savewebhookdataapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        String token = paramObj.getString("_t");
        WebhookConfigVo webhookConfigVo = webhookMapper.getWebhookByUrl(token);
        if (webhookConfigVo != null) {
            AppVo appVo = appMapper.getAppById(webhookConfigVo.getAppId());
            IWebhookHandler handler = WebhookFactory.getHandler(appVo.getType());
            if (handler != null) {
                WebhookDataVo webhookDataVo = handler.makeup(paramObj, webhookConfigVo);
                if (webhookDataVo != null) {
                    webhookDataVo.setData(paramObj);
                    webhookDataVo.setAppId(webhookConfigVo.getAppId());
                    webhookMapper.insertWebhookData(webhookDataVo);
                    return webhookDataVo.getId();
                }
            }
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/webhook/push";
    }
}
