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

package neatlogic.module.rdm.webhook;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.RequestContext;
import neatlogic.framework.rdm.dto.WebhookConfigVo;
import neatlogic.framework.rdm.dto.WebhookDataVo;
import neatlogic.framework.rdm.enums.AppType;
import neatlogic.framework.rdm.webhook.IWebhookHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class GitlabWebhookHandler implements IWebhookHandler {
    @Override
    public String getName() {
        return AppType.GITLAB.getName();
    }

    @Override
    public WebhookDataVo makeup(JSONObject paramObj, WebhookConfigVo webhookConfigVo) {
        HttpServletRequest request = RequestContext.get().getRequest();
        String event = request.getHeader("X-Gitlab-Event");
        String token = request.getHeader("X-Gitlab-Token");
        if (StringUtils.isBlank(token) || webhookConfigVo.getSecretToken().equalsIgnoreCase(token)) {
            if (StringUtils.isNotBlank(event) && "Push Hook".equalsIgnoreCase(event)) {
                WebhookDataVo webhookDataVo = new WebhookDataVo();
                webhookDataVo.setEvent(event);
                webhookDataVo.setUserId(paramObj.getString("user_username"));
                webhookDataVo.setUserName(paramObj.getString("user_name"));
                webhookDataVo.setEmail(paramObj.getString("user_email"));
                return webhookDataVo;
            }
        }
        return null;
    }


}
