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
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.module.rdm.dao.mapper.IssueMapper;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IssueStatHandlerBase {
    @Resource
    protected IssueMapper issueMapper;

    protected IssueStatResultVo createResult(IssueStatContextVo context, String type, String title) {
        IssueStatResultVo resultVo = new IssueStatResultVo();
        resultVo.setStatKey(context.getStatKey());
        resultVo.setConfigured(context.getStatField() != null);
        resultVo.setType(type);
        resultVo.setTitle(title);
        return resultVo;
    }

    protected IssueVo toIssueVo(IssueStatContextVo context) {
        return context.toIssueVo();
    }

    protected int getTotalCount(IssueStatContextVo context) {
        return getCachedInt(context, "totalCount", () -> issueMapper.getIssueOverviewTotalCount(toIssueVo(context)));
    }

    protected int getDoneCount(IssueStatContextVo context) {
        return getCachedInt(context, "doneCount", () -> issueMapper.getIssueOverviewDoneCount(toIssueVo(context)));
    }

    protected int getCachedInt(IssueStatContextVo context, String key, IntLoader loader) {
        Map<String, Object> cache = context.getCache();
        if (!cache.containsKey(key)) {
            cache.put(key, getInt(loader.load()));
        }
        return getInt(cache.get(key));
    }

    protected JSONArray toJsonArray(List<HashMap<String, Object>> rowList) {
        JSONArray array = new JSONArray();
        if (rowList == null) {
            return array;
        }
        for (HashMap<String, Object> row : rowList) {
            JSONObject rowObj = new JSONObject();
            row.forEach((key, value) -> rowObj.put(normalizeKey(key), value));
            array.add(rowObj);
        }
        return array;
    }

    protected List<HashMap<String, Object>> getCustomAttrValueList(IssueStatContextVo context, AppAttrVo appAttrVo) {
        if (context == null || appAttrVo == null) {
            return null;
        }
        IssueVo issueVo = toIssueVo(context);
        return issueMapper.getIssueOverviewCustomAttrValueList(issueVo, appAttrVo.getTableName(), appAttrVo.getId());
    }

    protected Object getValue(HashMap<String, Object> row, String key) {
        if (row == null || key == null) {
            return null;
        }
        String snakeKey = key.replaceAll("([A-Z])", "_$1").toLowerCase();
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            String currentKey = entry.getKey();
            if (currentKey != null && (currentKey.equals(key) || currentKey.equalsIgnoreCase(key) || currentKey.equalsIgnoreCase(snakeKey))) {
                return entry.getValue();
            }
        }
        return null;
    }

    private String normalizeKey(String key) {
        if (key == null) {
            return null;
        }
        String lowerKey = key.replace("_", "").toLowerCase();
        if ("issuecount".equals(lowerKey)) {
            return "issueCount";
        } else if ("totalcount".equals(lowerKey)) {
            return "totalCount";
        } else if ("overduecount".equals(lowerKey)) {
            return "overdueCount";
        } else if ("isend".equals(lowerKey)) {
            return "isEnd";
        }
        return key.toLowerCase();
    }

    protected int getInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    protected interface IntLoader {
        Object load();
    }
}
