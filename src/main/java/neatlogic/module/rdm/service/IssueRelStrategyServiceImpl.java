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

package neatlogic.module.rdm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IssueRelStrategyServiceImpl implements IssueRelStrategyService {
    private static final String REL_STRATEGY_LIST = "relStrategyList";

    @Resource
    private AppMapper appMapper;

    @Override
    public String getAction(Long fromAppId, Long toAppId, String relType) {
        if (fromAppId == null || toAppId == null || StringUtils.isBlank(relType)) {
            return ACTION_ORIGINAL;
        }
        AppVo fromApp = appMapper.getAppById(fromAppId);
        AppVo toApp = appMapper.getAppById(toAppId);
        if (fromApp == null || toApp == null) {
            return ACTION_ORIGINAL;
        }
        JSONArray relStrategyList = fromApp.getConfig().getJSONArray(REL_STRATEGY_LIST);
        if (CollectionUtils.isEmpty(relStrategyList)) {
            return ACTION_ORIGINAL;
        }
        for (int i = 0; i < relStrategyList.size(); i++) {
            JSONObject strategy = relStrategyList.getJSONObject(i);
            if (strategy == null) {
                continue;
            }
            boolean isMatchedByType = toApp.getType().equalsIgnoreCase(strategy.getString("toAppType"));
            boolean isMatchedById = toAppId.equals(strategy.getLong("toAppId"));
            if ((isMatchedByType || isMatchedById) && relType.equalsIgnoreCase(strategy.getString("relType"))) {
                return ACTION_COPY.equals(strategy.getString("action")) ? ACTION_COPY : ACTION_ORIGINAL;
            }
        }
        return ACTION_ORIGINAL;
    }
}
