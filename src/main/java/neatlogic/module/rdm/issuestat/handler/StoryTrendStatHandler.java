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
import neatlogic.framework.rdm.dto.IssueStatContextVo;
import neatlogic.framework.rdm.dto.IssueStatResultVo;
import neatlogic.framework.rdm.enums.AppStatAttrType;
import neatlogic.module.rdm.issuestat.IIssueStatHandler;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StoryTrendStatHandler extends IssueStatHandlerBase implements IIssueStatHandler {
    @Override
    public String getStatKey() {
        return AppStatAttrType.STORY_TREND.getValue();
    }

    @Override
    public IssueStatResultVo calculate(IssueStatContextVo context) {
        IssueStatResultVo resultVo = createResult(context, "trend", "近7个月需求/逾期趋势");
        resultVo.setDataList(getTrendList(issueMapper.getIssueOverviewTrendList(toIssueVo(context))));
        return resultVo;
    }

    private JSONArray getTrendList(List<HashMap<String, Object>> rowList) {
        Map<String, HashMap<String, Object>> rowMap = new HashMap<>();
        if (rowList != null) {
            for (HashMap<String, Object> row : rowList) {
                Object month = getValue(row, "month");
                if (month != null) {
                    rowMap.put(month.toString(), row);
                }
            }
        }
        JSONArray trendList = new JSONArray();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -6);
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < 7; i++) {
            String month = monthFormat.format(calendar.getTime());
            HashMap<String, Object> row = rowMap.get(month);
            JSONObject monthObj = new JSONObject();
            monthObj.put("month", month);
            monthObj.put("totalCount", row == null ? 0 : getInt(getValue(row, "totalCount")));
            monthObj.put("overdueCount", row == null ? 0 : getInt(getValue(row, "overdueCount")));
            trendList.add(monthObj);
            calendar.add(Calendar.MONTH, 1);
        }
        return trendList;
    }
}
