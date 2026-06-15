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

package neatlogic.module.rdm.issuestat.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.IssueStatContextVo;
import neatlogic.framework.rdm.dto.IssueStatResultVo;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.*;

public abstract class BugCustomAttrStatHandlerBase extends IssueStatHandlerBase {
    private static final String[] CHART_COLOR_ARRAY = new String[]{"#00C2B1", "#9B1DEA", "#F16B62", "#607D8B", "#FFB54D", "#2D7FF9"};
    @Resource
    private AttrMapper attrMapper;

    protected abstract String getTargetStatKey();

    protected IssueStatResultVo createUnconfiguredResult(IssueStatContextVo context, String type, String title) {
        IssueStatResultVo resultVo = createResult(context, type, title);
        resultVo.setConfigured(false);
        return resultVo;
    }

    protected AppAttrVo getTargetAppAttr(IssueStatContextVo context) {
        if (context == null || context.getAppId() == null || StringUtils.isBlank(getTargetStatKey())) {
            return null;
        }
        return attrMapper.getAttrByAppIdAndStatKey(context.getAppId(), getTargetStatKey());
    }

    protected IssueStatResultVo calculateDistribution(IssueStatContextVo context, String title) {
        AppAttrVo appAttrVo = getTargetAppAttr(context);
        if (appAttrVo == null) {
            return createUnconfiguredResult(context, "column", title);
        }
        IssueStatResultVo resultVo = createResult(context, "column", title);
        resultVo.setConfigured(true);
        resultVo.setFilterField("attr_" + appAttrVo.getId());
        resultVo.setFilterAttrId(appAttrVo.getId());
        resultVo.setDataList(buildDistributionData(getCustomAttrValueList(context, appAttrVo)));
        return resultVo;
    }

    protected IssueStatResultVo calculateCountGreaterThanZero(IssueStatContextVo context, String title) {
        AppAttrVo appAttrVo = getTargetAppAttr(context);
        if (appAttrVo == null) {
            return createUnconfiguredResult(context, "metric", title);
        }
        int count = 0;
        List<HashMap<String, Object>> rowList = getCustomAttrValueList(context, appAttrVo);
        if (CollectionUtils.isNotEmpty(rowList)) {
            for (HashMap<String, Object> row : rowList) {
                if (hasNumberGreaterThanZero(getValue(row, "value"))) {
                    count++;
                }
            }
        }
        IssueStatResultVo resultVo = createResult(context, "metric", title);
        resultVo.setConfigured(true);
        resultVo.setFilterField("attr_" + appAttrVo.getId());
        resultVo.setFilterAttrId(appAttrVo.getId());
        resultVo.setValue(count);
        return resultVo;
    }

    private JSONArray buildDistributionData(List<HashMap<String, Object>> rowList) {
        Map<String, Integer> countMap = new LinkedHashMap<>();
        if (CollectionUtils.isNotEmpty(rowList)) {
            for (HashMap<String, Object> row : rowList) {
                for (String value : getValueList(getValue(row, "value"))) {
                    countMap.put(value, countMap.getOrDefault(value, 0) + 1);
                }
            }
        }
        JSONArray dataList = new JSONArray();
        int index = 0;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            JSONObject dataObj = new JSONObject();
            dataObj.put("id", entry.getKey());
            dataObj.put("name", entry.getKey());
            dataObj.put("label", entry.getKey());
            dataObj.put("value", entry.getKey());
            dataObj.put("issueCount", entry.getValue());
            dataObj.put("color", CHART_COLOR_ARRAY[index % CHART_COLOR_ARRAY.length]);
            dataList.add(dataObj);
            index++;
        }
        return dataList;
    }

    private boolean hasNumberGreaterThanZero(Object value) {
        for (String valueText : getValueList(value)) {
            try {
                if (Double.parseDouble(valueText) > 0) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
                // 非数字值不参与重开次数统计。
            }
        }
        return false;
    }

    private List<String> getValueList(Object value) {
        List<String> valueList = new ArrayList<>();
        if (value == null || StringUtils.isBlank(value.toString())) {
            return valueList;
        }
        String valueText = value.toString();
        if (valueText.startsWith("[") && valueText.endsWith("]")) {
            try {
                JSONArray array = JSONArray.parseArray(valueText);
                for (Object item : array) {
                    if (item != null && StringUtils.isNotBlank(item.toString())) {
                        valueList.add(item.toString());
                    }
                }
                return valueList;
            } catch (Exception ignored) {
                // 不是合法JSON数组时按普通文本处理。
            }
        }
        valueList.add(valueText);
        return valueList;
    }
}
