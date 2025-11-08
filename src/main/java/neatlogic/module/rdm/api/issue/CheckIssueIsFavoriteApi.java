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
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class CheckIssueIsFavoriteApi extends PrivateApiComponentBase {


    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "nmrai.checkissueisfavoriteapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "term.rdm.issueid")})
    @Output({@Param(type = ApiParamType.INTEGER, name = "Return", desc = "nmrai.checkissueisfavoriteapi.output.param.desc")})
    @Description(desc = "nmrai.checkissueisfavoriteapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        return issueMapper.checkIssueIsFavorite(paramObj.getLong("id"), UserContext.get().getUserUuid(true));
    }

    @Override
    public String getToken() {
        return "/rdm/issue/favorite/check";
    }
}
