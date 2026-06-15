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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.AppStatAttrType;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.exception.IssueNotAuthSearchException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.CatalogMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.issuestat.IIssueStatHandler;
import neatlogic.module.rdm.issuestat.IssueStatFieldResolver;
import neatlogic.module.rdm.issuestat.IssueStatHandlerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetIssueOverviewApi extends PrivateApiComponentBase {
    @Resource
    private IssueMapper issueMapper;
    @Resource
    private CatalogMapper catalogMapper;
    @Resource
    private AppMapper appMapper;
    @Resource
    private IssueStatFieldResolver issueStatFieldResolver;

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
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "common.catalog"),
            @Param(name = "statKeyList", type = ApiParamType.JSONARRAY, desc = "统计属性标识列表")})
    @Output({@Param(name = "totalCount", type = ApiParamType.INTEGER, desc = "需求总数"),
            @Param(name = "overdueCount", type = ApiParamType.INTEGER, desc = "逾期需求总数"),
            @Param(name = "doneCount", type = ApiParamType.INTEGER, desc = "已完成需求总数"),
            @Param(name = "completeRate", type = ApiParamType.DOUBLE, desc = "完成率"),
            @Param(name = "highRiskCount", type = ApiParamType.INTEGER, desc = "高风险需求数"),
            @Param(name = "statusList", type = ApiParamType.JSONARRAY, desc = "状态分布"),
            @Param(name = "priorityList", type = ApiParamType.JSONARRAY, desc = "优先级分布"),
            @Param(name = "trendList", type = ApiParamType.JSONARRAY, desc = "近7个月需求与逾期趋势"),
            @Param(name = "statResultList", type = ApiParamType.JSONARRAY, desc = "统计块列表")})
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
        JSONArray statKeyArray = paramObj.getJSONArray("statKeyList");
        if (CollectionUtils.isNotEmpty(statKeyArray)) {
            return getStatOverview(issueVo, statKeyArray);
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

    private JSONObject getStatOverview(IssueVo issueVo, JSONArray statKeyArray) {
        AppVo appVo = appMapper.getAppById(issueVo.getAppId());
        List<IssueStatResultVo> statResultList = new ArrayList<>();
        IssueStatContextVo contextVo = new IssueStatContextVo();
        contextVo.setProjectId(issueVo.getProjectId());
        contextVo.setAppId(issueVo.getAppId());
        contextVo.setCatalogLft(issueVo.getCatalogLft());
        contextVo.setCatalogRht(issueVo.getCatalogRht());
        for (int i = 0; i < statKeyArray.size(); i++) {
            String statKey = statKeyArray.getString(i);
            AppStatAttrType statAttrType = AppStatAttrType.get(statKey);
            if (appVo == null || statAttrType == null || !statAttrType.getAppType().equalsIgnoreCase(appVo.getType())) {
                statResultList.add(getUnconfiguredResult(statKey, statAttrType));
                continue;
            }
            contextVo.setStatKey(statKey);
            IssueStatFieldVo statFieldVo = issueStatFieldResolver.resolve(issueVo.getAppId(), statKey);
            contextVo.setStatField(statFieldVo);
            IIssueStatHandler handler = IssueStatHandlerFactory.getHandler(statKey);
            if (handler == null) {
                statResultList.add(getUnconfiguredResult(statKey, statAttrType));
                continue;
            }
            statResultList.add(handler.calculate(contextVo));
        }
        JSONObject resultObj = new JSONObject();
        JSONArray statResultArray = JSONArray.parseArray(JSON.toJSONString(statResultList, SerializerFeature.DisableCircularReferenceDetect));
        resultObj.put("statResultList", statResultArray);
        fillLegacyOverviewField(resultObj, statResultArray, contextVo);
        fillMissingLegacyOverviewField(resultObj, contextVo);
        return resultObj;
    }

    private IssueStatResultVo getUnconfiguredResult(String statKey, AppStatAttrType statAttrType) {
        IssueStatResultVo resultVo = new IssueStatResultVo();
        resultVo.setStatKey(statKey);
        resultVo.setConfigured(false);
        resultVo.setTitle(statAttrType == null ? statKey : statAttrType.getText());
        return resultVo;
    }

    private void fillLegacyOverviewField(JSONObject resultObj, JSONArray statResultArray, IssueStatContextVo contextVo) {
        for (int i = 0; i < statResultArray.size(); i++) {
            JSONObject statObj = statResultArray.getJSONObject(i);
            String statKey = statObj.getString("statKey");
            if (AppStatAttrType.STORY_TOTAL.getValue().equals(statKey)) {
                resultObj.put("totalCount", statObj.getInteger("value"));
            } else if (AppStatAttrType.STORY_OVERDUE.getValue().equals(statKey)) {
                resultObj.put("overdueCount", statObj.getInteger("value"));
            } else if (AppStatAttrType.STORY_COMPLETE_RATE.getValue().equals(statKey)) {
                resultObj.put("completeRate", statObj.getDouble("value"));
                resultObj.put("doneCount", issueMapper.getIssueOverviewDoneCount(contextVo.toIssueVo()));
            } else if (AppStatAttrType.STORY_HIGH_RISK.getValue().equals(statKey)) {
                resultObj.put("highRiskCount", statObj.getInteger("value"));
            } else if (AppStatAttrType.STORY_STATUS_DISTRIBUTION.getValue().equals(statKey)) {
                resultObj.put("statusList", cloneJsonArray(statObj.getJSONArray("dataList")));
            } else if (AppStatAttrType.STORY_PRIORITY_DISTRIBUTION.getValue().equals(statKey)) {
                resultObj.put("priorityList", cloneJsonArray(statObj.getJSONArray("dataList")));
            } else if (AppStatAttrType.STORY_TREND.getValue().equals(statKey)) {
                resultObj.put("trendList", cloneJsonArray(statObj.getJSONArray("dataList")));
            }
        }
    }

    private JSONArray cloneJsonArray(JSONArray dataList) {
        if (dataList == null) {
            return new JSONArray();
        }
        return JSONArray.parseArray(JSON.toJSONString(dataList, SerializerFeature.DisableCircularReferenceDetect));
    }

    private void fillMissingLegacyOverviewField(JSONObject resultObj, IssueStatContextVo contextVo) {
        IssueVo issueVo = contextVo.toIssueVo();
        if (!resultObj.containsKey("totalCount") || !resultObj.containsKey("overdueCount") || !resultObj.containsKey("completeRate") || !resultObj.containsKey("highRiskCount")) {
            HashMap<String, Object> summary = issueMapper.getIssueOverviewSummary(issueVo);
            int totalCount = getInt(summary, "totalCount");
            int doneCount = getInt(summary, "doneCount");
            resultObj.putIfAbsent("totalCount", totalCount);
            resultObj.putIfAbsent("overdueCount", getInt(summary, "overdueCount"));
            resultObj.putIfAbsent("doneCount", doneCount);
            resultObj.putIfAbsent("completeRate", totalCount > 0 ? (double) doneCount / totalCount : 0D);
            resultObj.putIfAbsent("highRiskCount", getInt(summary, "highRiskCount"));
        }
        if (!resultObj.containsKey("statusList")) {
            resultObj.put("statusList", toJsonArray(issueMapper.getIssueOverviewStatusList(issueVo)));
        }
        if (!resultObj.containsKey("priorityList")) {
            resultObj.put("priorityList", toJsonArray(issueMapper.getIssueOverviewPriorityList(issueVo)));
        }
        if (!resultObj.containsKey("trendList")) {
            resultObj.put("trendList", getTrendList(issueMapper.getIssueOverviewTrendList(issueVo)));
        }
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
