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

package neatlogic.module.rdm.api.issue;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class ToggleIssueIsFavoriteApi extends PrivateApiComponentBase {
    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "nmrai.toggleissueisfavoriteapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "issueId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.issueid"),
            @Param(name = "isFavorite", type = ApiParamType.INTEGER, isRequired = true, rule = "0,1", desc = "nmrai.toggleissueisfavoriteapi.input.param.desc.isfavorite")
    })
    @Description(desc = "nmrai.toggleissueisfavoriteapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long issueId = paramObj.getLong("issueId");
        Integer isFavorite = paramObj.getInteger("isFavorite");
        String userId = UserContext.get().getUserUuid(true);
        if (isFavorite.equals(1)) {
            if (issueMapper.checkIssueIsFavorite(issueId, userId) == 0) {
                issueMapper.insertIssueIsFavorite(issueId, userId);
            }
        } else {
            issueMapper.deleteFavoriteIssue(issueId, userId);
        }
        return null;
    }


    @Override
    public String getToken() {
        return "/rdm/issue/favorite/toggle";
    }

}
