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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppCatalogVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.IssueNotAuthSearchException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.CatalogMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetIssueOverviewApi extends PrivateApiComponentBase {
    @Resource
    private IssueMapper issueMapper;
    @Resource
    private CatalogMapper catalogMapper;

    @Override
    public String getName() {
        return "获取RDM Issue概览";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.projectid"),
            @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "nmraa.getappapi.input.param.desc"),
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "common.catalog")})
    @Output({@Param(name = "totalCount", type = ApiParamType.INTEGER, desc = "需求总数"),
            @Param(name = "overdueCount", type = ApiParamType.INTEGER, desc = "逾期需求总数"),
            @Param(name = "doneCount", type = ApiParamType.INTEGER, desc = "已完成需求总数"),
            @Param(name = "completeRate", type = ApiParamType.DOUBLE, desc = "完成率"),
            @Param(name = "highRiskCount", type = ApiParamType.INTEGER, desc = "高风险需求数"),
            @Param(name = "statusList", type = ApiParamType.JSONARRAY, desc = "状态分布"),
            @Param(name = "priorityList", type = ApiParamType.JSONARRAY, desc = "优先级分布"),
            @Param(name = "trendList", type = ApiParamType.JSONARRAY, desc = "近7个月需求与逾期趋势")})
    @Description(desc = "获取RDM Issue概览接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("projectId");
        if (!ProjectAuthManager.checkProjectAuth(projectId, ProjectUserType.OWNER, ProjectUserType.LEADER, ProjectUserType.MEMBER)) {
            throw new IssueNotAuthSearchException();
        }
        IssueVo issueVo = new IssueVo();
        issueVo.setProjectId(projectId);
        issueVo.setAppId(paramObj.getLong("appId"));
        Long catalog = paramObj.getLong("catalog");
        if (catalog != null) {
            AppCatalogVo catalogVo = catalogMapper.getAppCatalogById(catalog);
            if (catalogVo != null) {
                issueVo.setCatalogLft(catalogVo.getLft());
                issueVo.setCatalogRht(catalogVo.getRht());
            }
        }

        HashMap<String, Object> summary = issueMapper.getIssueOverviewSummary(issueVo);
        int totalCount = getInt(summary, "totalCount");
        int doneCount = getInt(summary, "doneCount");
        JSONObject resultObj = new JSONObject();
        resultObj.put("totalCount", totalCount);
        resultObj.put("overdueCount", getInt(summary, "overdueCount"));
        resultObj.put("doneCount", doneCount);
        resultObj.put("completeRate", totalCount > 0 ? (double) doneCount / totalCount : 0D);
        resultObj.put("highRiskCount", getInt(summary, "highRiskCount"));
        resultObj.put("statusList", toJsonArray(issueMapper.getIssueOverviewStatusList(issueVo)));
        resultObj.put("priorityList", toJsonArray(issueMapper.getIssueOverviewPriorityList(issueVo)));
        resultObj.put("trendList", getTrendList(issueMapper.getIssueOverviewTrendList(issueVo)));
        return resultObj;
    }

    private JSONArray getTrendList(List<HashMap<String, Object>> rowList) {
        Map<String, HashMap<String, Object>> rowMap = new HashMap<>();
        for (HashMap<String, Object> row : rowList) {
            Object month = row.get("month");
            if (month != null) {
                rowMap.put(month.toString(), row);
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
            monthObj.put("totalCount", getInt(row, "totalCount"));
            monthObj.put("overdueCount", getInt(row, "overdueCount"));
            trendList.add(monthObj);
            calendar.add(Calendar.MONTH, 1);
        }
        return trendList;
    }

    private JSONArray toJsonArray(List<HashMap<String, Object>> rowList) {
        JSONArray array = new JSONArray();
        for (HashMap<String, Object> row : rowList) {
            JSONObject rowObj = new JSONObject();
            row.forEach(rowObj::put);
            array.add(rowObj);
        }
        return array;
    }

    private int getInt(Map<String, Object> map, String key) {
        if (map == null || map.get(key) == null) {
            return 0;
        }
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    @Override
    public String getToken() {
        return "/rdm/issue/overview/get";
    }
}
