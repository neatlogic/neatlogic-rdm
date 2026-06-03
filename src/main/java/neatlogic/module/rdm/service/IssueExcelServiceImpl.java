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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.exception.type.ParamIrregularException;
import neatlogic.framework.rdm.attrhandler.code.AttrHandlerFactory;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.enums.SystemAttrType;
import neatlogic.framework.rdm.exception.IssueImportException;
import neatlogic.framework.rdm.exception.IssueNotFoundException;
import neatlogic.framework.rdm.exception.ProjectNotAuthIssueException;
import neatlogic.framework.util.FileUtil;
import neatlogic.framework.util.Md5Util;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IssueExcelServiceImpl implements IssueExcelService {
    private static final int LABEL_ROW_INDEX = 0;
    private static final int HELP_ROW_INDEX = 1;
    private static final int KEY_ROW_INDEX = 2;
    private static final int DATA_START_ROW_INDEX = 3;
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CONTENT = "content";
    private static final String SYS_PREFIX = "sys:";
    private static final String ATTR_PREFIX = "attr:";

    @Resource
    private AppMapper appMapper;
    @Resource
    private AttrMapper attrMapper;
    @Resource
    private IssueMapper issueMapper;
    @Resource
    private IssueService issueService;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private PriorityMapper priorityMapper;
    @Resource
    private IterationMapper iterationMapper;
    @Resource
    private CatalogMapper catalogMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public void exportIssue(JSONObject paramObj, HttpServletResponse response) throws Exception {
        Long appId = paramObj.getLong("appId");
        AppVo appVo = getAppWithSystemAttr(appId);
        checkAppAuth(appVo);
        IssueConditionVo conditionVo = buildSearchCondition(paramObj, appVo);
        int rowNum = issueMapper.searchIssueCount(conditionVo);
        List<IssueVo> issueList = new ArrayList<>();
        if (rowNum > 0) {
            conditionVo.setCurrentPage(1);
            conditionVo.setMaxPageSize(Math.max(rowNum, 20));
            conditionVo.setPageSize(rowNum);
            List<Long> idList = issueMapper.searchIssueId(conditionVo);
            if (CollectionUtils.isNotEmpty(idList)) {
                for (Long id : idList) {
                    IssueVo issueVo = issueService.getIssueById(id);
                    if (issueVo != null) {
                        issueList.add(issueVo);
                    }
                }
            }
        }
        Workbook workbook = buildWorkbook(appVo, issueList);
        writeWorkbook(response, workbook, appVo.getName() + "_导出_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx");
    }

    @Override
    public void downloadImportTemplate(Long appId, HttpServletResponse response) throws Exception {
        AppVo appVo = getAppWithSystemAttr(appId);
        checkAppAuth(appVo);
        Workbook workbook = buildWorkbook(appVo, new ArrayList<>());
        writeWorkbook(response, workbook, appVo.getName() + "_导入模板.xlsx");
    }

    @Override
    public JSONObject importIssue(Long appId, MultipartFile multipartFile) throws Exception {
        AppVo appVo = getAppWithSystemAttr(appId);
        checkAppAuth(appVo);
        List<ImportRow> importRowList = parseImportRowList(appVo, multipartFile);
        markNoChangeRow(importRowList);
        List<ImportRow> effectiveImportRowList = getEffectiveImportRowList(importRowList);
        JSONObject resultObj = initImportResult();
        fillImportRowSummary(resultObj, importRowList, effectiveImportRowList);
        if (hasFailedRow(effectiveImportRowList)) {
            fillFailedImportResult(resultObj, effectiveImportRowList);
            return resultObj;
        }
        saveImportRowList(resultObj, effectiveImportRowList);
        fillImportRowSummary(resultObj, importRowList, effectiveImportRowList);
        return resultObj;
    }

    @Override
    public JSONObject previewImportIssue(Long appId, MultipartFile multipartFile) throws Exception {
        AppVo appVo = getAppWithSystemAttr(appId);
        checkAppAuth(appVo);
        List<ImportRow> importRowList = parseImportRowList(appVo, multipartFile);
        markNoChangeRow(importRowList);
        List<ImportRow> effectiveImportRowList = getEffectiveImportRowList(importRowList);
        clearPreviewNewIssueId(effectiveImportRowList);
        JSONObject resultObj = initImportResult();
        fillImportRowSummary(resultObj, importRowList, effectiveImportRowList);
        if (hasFailedRow(effectiveImportRowList)) {
            fillFailedImportResult(resultObj, effectiveImportRowList);
        } else {
            fillPreviewSuccessResult(resultObj, effectiveImportRowList);
        }
        return resultObj;
    }

    private List<ImportRow> parseImportRowList(AppVo appVo, MultipartFile multipartFile) throws Exception {
        if (multipartFile == null || StringUtils.isBlank(multipartFile.getOriginalFilename())
                || !multipartFile.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            throw new ParamIrregularException("fileList", "xlsx");
        }
        List<ExcelColumn> configuredColumnList = getColumnList(appVo);
        Map<String, ExcelColumn> configuredColumnMap = configuredColumnList.stream().collect(Collectors.toMap(ExcelColumn::getKey, column -> column, (a, b) -> a));
        List<ImportRow> importRowList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getRow(KEY_ROW_INDEX) == null) {
                throw new IssueImportException("模板格式不正确，请重新下载模板");
            }
            List<ExcelColumn> importColumnList = getImportColumnList(sheet.getRow(KEY_ROW_INDEX), configuredColumnMap);
            if (importColumnList.stream().noneMatch(column -> column != null && KEY_NAME.equals(column.getKey()))) {
                throw new IssueImportException("模板格式不正确，缺少标题列");
            }
            ImportReferenceData referenceData = new ImportReferenceData(appVo);
            for (int i = DATA_START_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (isBlankRow(row, importColumnList)) {
                    continue;
                }
                ImportRow importRow = buildImportRow(appVo, row, importColumnList, referenceData);
                importRowList.add(importRow);
            }
        } catch (IssueImportException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IssueImportException(ex.getMessage());
        }
        return importRowList;
    }

    private JSONObject initImportResult() {
        JSONObject resultObj = new JSONObject();
        resultObj.put("status", "success");
        resultObj.put("totalCount", 0);
        resultObj.put("fileRowCount", 0);
        resultObj.put("successCount", 0);
        resultObj.put("failedCount", 0);
        resultObj.put("insertCount", 0);
        resultObj.put("updateCount", 0);
        resultObj.put("unchangedCount", 0);
        resultObj.put("rowList", new JSONArray());
        return resultObj;
    }

    private void fillImportRowSummary(JSONObject resultObj, List<ImportRow> importRowList, List<ImportRow> effectiveImportRowList) {
        resultObj.put("fileRowCount", importRowList.size());
        resultObj.put("totalCount", effectiveImportRowList.size());
        resultObj.put("unchangedCount", importRowList.stream().filter(ImportRow::getNoChange).count());
    }

    private ImportRow buildImportRow(AppVo appVo, Row row, List<ExcelColumn> importColumnList, ImportReferenceData referenceData) {
        ImportRow importRow = new ImportRow(row.getRowNum() + 1);
        Map<String, String> valueMap = getRowValueMap(row, importColumnList);
        String rawId = valueMap.get(KEY_ID);
        importRow.getResultObj().put("id", rawId);
        importRow.getResultObj().put("name", valueMap.get(KEY_NAME));
        importRow.getResultObj().put("action", StringUtils.isBlank(rawId) ? "add" : "update");
        importRow.getResultObj().put("actionText", StringUtils.isBlank(rawId) ? "新增" : "更新");
        try {
            IssueVo issueVo = buildIssueFromRow(appVo, row, importColumnList, referenceData);
            issueVo.formatAttr();
            importRow.setIssueVo(issueVo);
            importRow.getResultObj().put("id", issueVo.getId());
            importRow.getResultObj().put("name", issueVo.getName());
            importRow.getResultObj().put("action", issueVo.getIsNew() ? "add" : "update");
            importRow.getResultObj().put("actionText", issueVo.getIsNew() ? "新增" : "更新");
            importRow.setStatus("success");
        } catch (Exception ex) {
            importRow.addError(getImportErrorMessage(ex));
        }
        return importRow;
    }

    private Map<String, String> getRowValueMap(Row row, List<ExcelColumn> importColumnList) {
        DataFormatter formatter = new DataFormatter();
        Map<String, String> valueMap = new HashMap<>();
        for (int i = 0; i < importColumnList.size(); i++) {
            ExcelColumn column = importColumnList.get(i);
            if (column != null) {
                valueMap.put(column.getKey(), formatter.formatCellValue(row.getCell(i)).trim());
            }
        }
        return valueMap;
    }

    private boolean hasFailedRow(List<ImportRow> importRowList) {
        return importRowList.stream().anyMatch(importRow -> "failed".equals(importRow.getStatus()));
    }

    private void markNoChangeRow(List<ImportRow> importRowList) {
        for (ImportRow importRow : importRowList) {
            IssueVo issueVo = importRow.getIssueVo();
            if (!"success".equals(importRow.getStatus()) || issueVo == null || issueVo.getIsNew()) {
                continue;
            }
            IssueVo oldIssue = issueService.getIssueById(issueVo.getId());
            if (oldIssue != null && !hasImportChange(oldIssue, issueVo)) {
                importRow.setNoChange(true);
            }
        }
    }

    private List<ImportRow> getEffectiveImportRowList(List<ImportRow> importRowList) {
        return importRowList.stream().filter(importRow -> !importRow.getNoChange()).collect(Collectors.toList());
    }

    private void clearPreviewNewIssueId(List<ImportRow> importRowList) {
        for (ImportRow importRow : importRowList) {
            IssueVo issueVo = importRow.getIssueVo();
            if (issueVo != null && issueVo.getIsNew()) {
                importRow.getResultObj().remove("id");
            }
        }
    }

    private boolean hasImportChange(IssueVo oldIssue, IssueVo newIssue) {
        if (newIssue.hasSubmittedField("name") && isStringChanged(oldIssue.getName(), newIssue.getName())) {
            return true;
        }
        if (newIssue.hasSubmittedField("content") && isStringChanged(oldIssue.getContent(), newIssue.getContent())) {
            return true;
        }
        if (newIssue.hasSubmittedField("status") && !Objects.equals(oldIssue.getStatus(), newIssue.getStatus())) {
            return true;
        }
        if (newIssue.hasSubmittedField("priority") && !Objects.equals(oldIssue.getPriority(), newIssue.getPriority())) {
            return true;
        }
        if (newIssue.hasSubmittedField("iteration") && !Objects.equals(oldIssue.getIteration(), newIssue.getIteration())) {
            return true;
        }
        if (newIssue.hasSubmittedField("catalog") && !Objects.equals(oldIssue.getCatalog(), newIssue.getCatalog())) {
            return true;
        }
        if (newIssue.hasSubmittedField("startDate") && !Objects.equals(oldIssue.getStartDate(), newIssue.getStartDate())) {
            return true;
        }
        if (newIssue.hasSubmittedField("endDate") && !Objects.equals(oldIssue.getEndDate(), newIssue.getEndDate())) {
            return true;
        }
        if (newIssue.hasSubmittedField("timecost") && !Objects.equals(oldIssue.getTimecost(), newIssue.getTimecost())) {
            return true;
        }
        if (newIssue.hasSubmittedField("tagList") && isStringListChanged(oldIssue.getTagList(), newIssue.getTagList())) {
            return true;
        }
        if (newIssue.hasSubmittedField("userIdList") && isStringListChanged(oldIssue.getUserIdList(), newIssue.getUserIdList())) {
            return true;
        }
        return hasImportAttrChange(oldIssue, newIssue);
    }

    private boolean hasImportAttrChange(IssueVo oldIssue, IssueVo newIssue) {
        if (!newIssue.hasSubmittedField("attrList") || CollectionUtils.isEmpty(newIssue.getAttrList())) {
            return false;
        }
        for (IssueAttrVo newAttr : newIssue.getAttrList()) {
            IssueAttrVo oldAttr = oldIssue.getAttr(newAttr.getAttrId());
            String oldValue = oldAttr == null ? null : oldAttr.getValue();
            if (!Objects.equals(oldValue, newAttr.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean isStringChanged(String oldValue, String newValue) {
        return !Objects.equals(StringUtils.defaultString(oldValue), StringUtils.defaultString(newValue));
    }

    private boolean isStringListChanged(List<String> oldList, List<String> newList) {
        List<String> oldValueList = oldList == null ? new ArrayList<>() : oldList;
        List<String> newValueList = newList == null ? new ArrayList<>() : newList;
        return !CollectionUtils.isEqualCollection(oldValueList, newValueList);
    }

    private void fillFailedImportResult(JSONObject resultObj, List<ImportRow> importRowList) {
        resultObj.put("status", "failed");
        resultObj.put("successCount", 0);
        resultObj.put("failedCount", importRowList.stream().filter(importRow -> "failed".equals(importRow.getStatus())).count());
        resultObj.put("rowList", getRowResultList(importRowList));
    }

    private void fillPreviewSuccessResult(JSONObject resultObj, List<ImportRow> importRowList) {
        int insertCount = 0;
        int updateCount = 0;
        for (ImportRow importRow : importRowList) {
            IssueVo issueVo = importRow.getIssueVo();
            if (issueVo == null) {
                continue;
            }
            if (issueVo.getIsNew()) {
                insertCount++;
            } else {
                updateCount++;
            }
        }
        resultObj.put("status", "success");
        resultObj.put("insertCount", insertCount);
        resultObj.put("updateCount", updateCount);
        resultObj.put("successCount", insertCount + updateCount);
        resultObj.put("failedCount", 0);
        resultObj.put("rowList", getRowResultList(importRowList));
    }

    private void saveImportRowList(JSONObject resultObj, List<ImportRow> importRowList) {
        int insertCount = 0;
        int updateCount = 0;
        for (ImportRow importRow : importRowList) {
            try {
                IssueVo issueVo = importRow.getIssueVo();
                boolean isNew = issueVo.getIsNew();
                issueService.saveIssue(issueVo);
                if (isNew) {
                    insertCount++;
                } else {
                    updateCount++;
                }
            } catch (Exception ex) {
                importRow.addError(getImportErrorMessage(ex));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                fillFailedImportResult(resultObj, importRowList);
                return;
            }
        }
        resultObj.put("status", "success");
        resultObj.put("insertCount", insertCount);
        resultObj.put("updateCount", updateCount);
        resultObj.put("successCount", insertCount + updateCount);
        resultObj.put("failedCount", 0);
        resultObj.put("rowList", getRowResultList(importRowList));
    }

    private JSONArray getRowResultList(List<ImportRow> importRowList) {
        JSONArray rowList = new JSONArray();
        for (ImportRow importRow : importRowList) {
            rowList.add(importRow.getResultObj());
        }
        return rowList;
    }

    private String getImportErrorMessage(Exception ex) {
        String message = ex.getMessage();
        if (StringUtils.isBlank(message)) {
            return "导入失败";
        }
        message = message.replaceFirst("^第\\d+行：", "");
        int sqlErrorIndex = message.indexOf("### Error");
        if (sqlErrorIndex > 0) {
            message = message.substring(0, sqlErrorIndex).trim();
        }
        return message;
    }

    private AppVo getAppWithSystemAttr(Long appId) {
        if (appId == null) {
            throw new ParamIrregularException("appId");
        }
        AppVo appVo = appMapper.getAppById(appId);
        if (appVo == null) {
            throw new ParamIrregularException("appId");
        }
        List<AppAttrVo> attrList = SystemAttrType.getSystemAttrList(appId);
        List<AppAttrVo> customAttrList = attrMapper.getAttrByAppId(appId);
        if (CollectionUtils.isNotEmpty(customAttrList)) {
            for (AppAttrVo attrVo : customAttrList) {
                if (Integer.valueOf(1).equals(attrVo.getIsActive())) {
                    attrList.add(attrVo);
                }
            }
        }
        appVo.setAttrList(attrList);
        return appVo;
    }

    private void checkAppAuth(AppVo appVo) {
        if (!ProjectAuthManager.checkAppAuth(appVo.getId(), ProjectUserType.MEMBER, ProjectUserType.OWNER, ProjectUserType.LEADER)) {
            throw new ProjectNotAuthIssueException();
        }
    }

    private IssueConditionVo buildSearchCondition(JSONObject paramObj, AppVo appVo) {
        if (paramObj.getLong("projectId") == null) {
            paramObj.put("projectId", appVo.getProjectId());
        }
        IssueConditionVo issueVo = JSON.toJavaObject(paramObj, IssueConditionVo.class);
        if (issueVo.getIsMyCreated() != null && issueVo.getIsMyCreated().equals(1)) {
            issueVo.setCreateUser(UserContext.get().getUserUuid(true));
        }
        if (issueVo.getIsMine() != null && issueVo.getIsMine().equals(1)) {
            issueVo.setUserIdList(Collections.singletonList(UserContext.get().getUserUuid(true)));
        }
        if (issueVo.getCatalog() != null) {
            AppCatalogVo catalogVo = catalogMapper.getAppCatalogById(issueVo.getCatalog());
            if (catalogVo != null) {
                issueVo.setCatalogLft(catalogVo.getLft());
                issueVo.setCatalogRht(catalogVo.getRht());
            }
        }
        List<AppAttrVo> attrList = attrMapper.getAttrByAppId(issueVo.getAppId());
        if (attrList == null) {
            attrList = new ArrayList<>();
        }
        if (CollectionUtils.isNotEmpty(attrList)) {
            for (AppAttrVo attr : attrList) {
                if (Integer.valueOf(0).equals(attr.getIsPrivate())) {
                    issueVo.addAppAttr(attr);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(issueVo.getAttrFilterList())) {
            Iterator<IssueAttrVo> iterator = issueVo.getAttrFilterList().iterator();
            while (iterator.hasNext()) {
                IssueAttrVo issueAttrVo = iterator.next();
                Optional<AppAttrVo> attr = attrList.stream().filter(d -> d.getId().equals(issueAttrVo.getAttrId())).findFirst();
                if (attr.isPresent()) {
                    issueAttrVo.setAttrType(attr.get().getType());
                } else {
                    iterator.remove();
                }
            }
        }
        return issueVo;
    }

    private Workbook buildWorkbook(AppVo appVo, List<IssueVo> issueList) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(StringUtils.defaultIfBlank(appVo.getName(), "issue"));
        List<ExcelColumn> columnList = getColumnList(appVo);
        CellStyle headerStyle = buildHeaderStyle(workbook);
        CellStyle helpStyle = buildHelpStyle(workbook);
        CellStyle textStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        textStyle.setDataFormat(dataFormat.getFormat("@"));
        Row labelRow = sheet.createRow(LABEL_ROW_INDEX);
        Row helpRow = sheet.createRow(HELP_ROW_INDEX);
        Row keyRow = sheet.createRow(KEY_ROW_INDEX);
        for (int i = 0; i < columnList.size(); i++) {
            ExcelColumn column = columnList.get(i);
            Cell labelCell = labelRow.createCell(i);
            labelCell.setCellValue(column.getLabel());
            labelCell.setCellStyle(headerStyle);
            Cell helpCell = helpRow.createCell(i);
            helpCell.setCellValue(StringUtils.defaultString(column.getHelp()));
            helpCell.setCellStyle(helpStyle);
            keyRow.createCell(i).setCellValue(column.getKey());
            sheet.setDefaultColumnStyle(i, textStyle);
            sheet.setColumnWidth(i, Math.max(15, Math.min(50, column.getLabel().length() + 8)) * 256);
        }
        keyRow.setZeroHeight(true);
        sheet.createFreezePane(0, HELP_ROW_INDEX + 1);
        int rowIndex = DATA_START_ROW_INDEX;
        for (IssueVo issueVo : issueList) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < columnList.size(); i++) {
                row.createCell(i).setCellValue(truncateCellValue(getExportValue(issueVo, columnList.get(i))));
            }
        }
        return workbook;
    }

    private CellStyle buildHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle buildHelpStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFont(font);
        style.setWrapText(true);
        return style;
    }

    private void writeWorkbook(HttpServletResponse response, Workbook workbook, String fileName) throws Exception {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", " attachment; filename=\"" + FileUtil.getEncodedFileName(fileName) + "\"");
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
        } finally {
            workbook.close();
        }
    }

    private List<ExcelColumn> getColumnList(AppVo appVo) {
        List<ExcelColumn> columnList = new ArrayList<>();
        columnList.add(new ExcelColumn(KEY_ID, "id", "有id表示更新；留空表示新增"));
        columnList.add(new ExcelColumn(KEY_NAME, "标题", "必填"));
        columnList.add(new ExcelColumn(KEY_CONTENT, "内容", "可填写文本或HTML内容"));
        if (CollectionUtils.isNotEmpty(appVo.getAttrList())) {
            for (AppAttrVo attr : appVo.getAttrList()) {
                if (!attr.getAllowImport() || isNameAttr(attr)) {
                    continue;
                }
                AttrType attrType = AttrType.get(attr.getType());
                if (attrType != null && Boolean.TRUE.equals(isPrivateAttr(attr))) {
                    columnList.add(new ExcelColumn(SYS_PREFIX + attrType.getName(), attr.getLabel(), attr.getImportHelp(), attr));
                } else if (isStatusAttr(attr)) {
                    columnList.add(new ExcelColumn(SYS_PREFIX + "status", attr.getLabel(), "请输入状态名称", attr));
                } else if (attr.getId() != null && attr.getId() > 0L && !Boolean.TRUE.equals(isPrivateAttr(attr))) {
                    columnList.add(new ExcelColumn(ATTR_PREFIX + attr.getId(), attr.getLabel(), attr.getImportHelp(), attr));
                }
            }
        }
        return columnList;
    }

    private Boolean isPrivateAttr(AppAttrVo attr) {
        return attr.getIsPrivate() != null && attr.getIsPrivate().equals(1);
    }

    private boolean isNameAttr(AppAttrVo attr) {
        return "name".equals(attr.getName()) || "_name".equals(attr.getType());
    }

    private boolean isStatusAttr(AppAttrVo attr) {
        return "status".equals(attr.getName()) || "_status".equals(attr.getType());
    }

    private List<ExcelColumn> getImportColumnList(Row keyRow, Map<String, ExcelColumn> configuredColumnMap) {
        List<ExcelColumn> columnList = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        short lastCellNum = keyRow.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            String key = formatter.formatCellValue(keyRow.getCell(i)).trim();
            if (StringUtils.isBlank(key)) {
                columnList.add(null);
            } else {
                columnList.add(configuredColumnMap.get(key));
            }
        }
        return columnList;
    }

    private boolean isBlankRow(Row row, List<ExcelColumn> importColumnList) {
        if (row == null) {
            return true;
        }
        DataFormatter formatter = new DataFormatter();
        for (int i = 0; i < importColumnList.size(); i++) {
            if (importColumnList.get(i) != null && StringUtils.isNotBlank(formatter.formatCellValue(row.getCell(i)))) {
                return false;
            }
        }
        return true;
    }

    private IssueVo buildIssueFromRow(AppVo appVo, Row row, List<ExcelColumn> importColumnList, ImportReferenceData referenceData) {
        int rowNum = row.getRowNum() + 1;
        Map<String, String> valueMap = getRowValueMap(row, importColumnList);
        Long id = parseLong(valueMap.get(KEY_ID), rowNum, "id");
        IssueVo issueVo;
        if (id != null) {
            IssueVo oldIssue = issueService.getIssueById(id);
            if (oldIssue == null) {
                throw new IssueNotFoundException(id);
            }
            if (!appVo.getId().equals(oldIssue.getAppId())) {
                throw new IssueImportException(rowNum, "id不属于当前应用");
            }
            issueVo = buildIssueForUpdate(oldIssue);
        } else {
            issueVo = new IssueVo();
            issueVo.getId();
            issueVo.setAppId(appVo.getId());
            issueVo.setCreateUser(UserContext.get().getUserUuid(true));
        }
        List<IssueAttrVo> attrList = new ArrayList<>();
        List<String> submittedFieldList = new ArrayList<>();
        for (ExcelColumn column : importColumnList) {
            if (column == null || !valueMap.containsKey(column.getKey())) {
                continue;
            }
            String value = valueMap.get(column.getKey());
            if (KEY_ID.equals(column.getKey())) {
                continue;
            } else if (KEY_NAME.equals(column.getKey())) {
                if (StringUtils.isBlank(value)) {
                    throw new IssueImportException(rowNum, "标题不能为空");
                }
                issueVo.setName(value);
                submittedFieldList.add("name");
            } else if (KEY_CONTENT.equals(column.getKey())) {
                issueVo.setContent(value);
                submittedFieldList.add("content");
            } else if (column.getKey().startsWith(SYS_PREFIX)) {
                applySystemField(issueVo, column.getKey().substring(SYS_PREFIX.length()), value, rowNum, referenceData);
                submittedFieldList.add(column.getKey().substring(SYS_PREFIX.length()));
            } else if (column.getKey().startsWith(ATTR_PREFIX)) {
                attrList.add(buildIssueAttr(issueVo.getId(), column.getAttr(), value, rowNum));
            }
        }
        if (CollectionUtils.isNotEmpty(attrList)) {
            issueVo.setAttrList(attrList);
            submittedFieldList.add("attrList");
        }
        if (issueVo.getIsNew() && issueVo.getStatus() == null) {
            issueVo.setStatus(referenceData.getStartStatusId());
        }
        issueVo.setSubmittedFieldList(submittedFieldList);
        return issueVo;
    }

    private IssueVo buildIssueForUpdate(IssueVo oldIssue) {
        IssueVo issueVo = new IssueVo();
        issueVo.setId(oldIssue.getId());
        issueVo.setAppId(oldIssue.getAppId());
        issueVo.setName(oldIssue.getName());
        issueVo.setPriority(oldIssue.getPriority());
        issueVo.setIteration(oldIssue.getIteration());
        issueVo.setStatus(oldIssue.getStatus());
        issueVo.setCatalog(oldIssue.getCatalog());
        issueVo.setStartDate(oldIssue.getStartDate());
        issueVo.setEndDate(oldIssue.getEndDate());
        issueVo.setContent(oldIssue.getContent());
        issueVo.setTimecost(oldIssue.getTimecost());
        return issueVo;
    }

    private void applySystemField(IssueVo issueVo, String fieldName, String value, int rowNum, ImportReferenceData referenceData) {
        switch (fieldName) {
            case "status":
                issueVo.setStatus(StringUtils.isBlank(value) ? null : referenceData.getStatusId(value, rowNum));
                break;
            case "priority":
                issueVo.setPriority(StringUtils.isBlank(value) ? null : referenceData.getPriorityId(value, rowNum));
                break;
            case "iteration":
                issueVo.setIteration(StringUtils.isBlank(value) ? null : referenceData.getIterationId(value, rowNum));
                break;
            case "catalog":
                issueVo.setCatalog(StringUtils.isBlank(value) ? null : referenceData.getCatalogId(value, rowNum));
                break;
            case "startDate":
                issueVo.setStartDate(StringUtils.isBlank(value) ? null : value);
                break;
            case "endDate":
                issueVo.setEndDate(StringUtils.isBlank(value) ? null : value);
                break;
            case "timecost":
                issueVo.setTimecost(StringUtils.isBlank(value) ? null : parseInteger(value, rowNum, "预估工时"));
                break;
            case "tagList":
                issueVo.setTagList(splitValue(value));
                break;
            case "userIdList":
                issueVo.setUserIdList(resolveUserList(splitValue(value), rowNum));
                break;
            default:
                break;
        }
    }

    private IssueAttrVo buildIssueAttr(Long issueId, AppAttrVo attr, String value, int rowNum) {
        IssueAttrVo issueAttrVo = new IssueAttrVo(attr.getId(), issueId, attr.getType(), attr.getConfig());
        if (StringUtils.isBlank(value)) {
            issueAttrVo.setValueList(new ArrayList<>());
            return issueAttrVo;
        }
        List<Object> valueList = new ArrayList<>();
        if (AttrType.UESR.getType().equals(attr.getType())) {
            valueList.addAll(resolveUserList(splitValue(value), rowNum));
        } else if (AttrType.SELECT.getType().equals(attr.getType())) {
            valueList.addAll(resolveSelectList(attr, splitValue(value), rowNum));
        } else {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(attr.getType());
            if (handler != null && handler.getIsArray()) {
                valueList.addAll(splitValue(value));
            } else {
                valueList.add(value);
            }
        }
        issueAttrVo.setValueList(valueList);
        return issueAttrVo;
    }

    private List<String> splitValue(String value) {
        List<String> valueList = new ArrayList<>();
        if (StringUtils.isNotBlank(value)) {
            for (String item : value.split(",")) {
                if (StringUtils.isNotBlank(item)) {
                    valueList.add(item.trim());
                }
            }
        }
        return valueList;
    }

    private List<String> resolveUserList(List<String> valueList, int rowNum) {
        List<String> userIdList = new ArrayList<>();
        for (String value : valueList) {
            if (Md5Util.isMd5(value) && userMapper.getUserByUuid(value) != null) {
                userIdList.add(value);
                continue;
            }
            UserVo userVo = userMapper.getUserByUserId(value);
            if (userVo == null) {
                throw new IssueImportException(rowNum, "用户账号“" + value + "”不存在");
            }
            userIdList.add(userVo.getUuid());
        }
        return userIdList;
    }

    private List<String> resolveSelectList(AppAttrVo attr, List<String> valueList, int rowNum) {
        if (attr.getConfig() == null || CollectionUtils.isEmpty(attr.getConfig().getJSONArray("members"))) {
            return valueList;
        }
        Set<String> memberSet = new HashSet<>(attr.getConfig().getJSONArray("members").toJavaList(String.class));
        for (String value : valueList) {
            if (!memberSet.contains(value)) {
                throw new IssueImportException(rowNum, "属性“" + attr.getLabel() + "”的选项“" + value + "”不存在");
            }
        }
        return valueList;
    }

    private String getExportValue(IssueVo issueVo, ExcelColumn column) {
        if (KEY_ID.equals(column.getKey())) {
            return issueVo.getId() == null ? "" : issueVo.getId().toString();
        } else if (KEY_NAME.equals(column.getKey())) {
            return issueVo.getName();
        } else if (KEY_CONTENT.equals(column.getKey())) {
            return issueVo.getContent();
        } else if (column.getKey().startsWith(SYS_PREFIX)) {
            return getSystemExportValue(issueVo, column.getKey().substring(SYS_PREFIX.length()));
        } else if (column.getKey().startsWith(ATTR_PREFIX)) {
            IssueAttrVo attrVo = issueVo.getAttr(column.getAttr().getId());
            return getAttrExportValue(attrVo, column.getAttr());
        }
        return "";
    }

    private String getSystemExportValue(IssueVo issueVo, String fieldName) {
        switch (fieldName) {
            case "status":
                return StringUtils.defaultIfBlank(issueVo.getStatusLabel(), issueVo.getStatusName());
            case "priority":
                return issueVo.getPriorityName();
            case "iteration":
                return issueVo.getIterationName();
            case "catalog":
                return issueVo.getCatalogName();
            case "startDate":
                return issueVo.getStartDate();
            case "endDate":
                return issueVo.getEndDate();
            case "timecost":
                return issueVo.getTimecost() == null ? "" : issueVo.getTimecost().toString();
            case "tagList":
                return CollectionUtils.isEmpty(issueVo.getTagList()) ? "" : String.join(",", issueVo.getTagList());
            case "userIdList":
                return joinUserIdList(issueVo.getUserIdList());
            default:
                return "";
        }
    }

    private String getAttrExportValue(IssueAttrVo attrVo, AppAttrVo attr) {
        if (attrVo == null || CollectionUtils.isEmpty(attrVo.getValueList())) {
            return "";
        }
        List<String> valueList = new ArrayList<>();
        for (Object value : attrVo.getValueList()) {
            if (value == null) {
                continue;
            }
            if (AttrType.UESR.getType().equals(attr.getType())) {
                valueList.add(getUserId(value.toString()));
            } else {
                valueList.add(value.toString());
            }
        }
        return String.join(",", valueList);
    }

    private String joinUserIdList(List<String> uuidList) {
        if (CollectionUtils.isEmpty(uuidList)) {
            return "";
        }
        return uuidList.stream().map(this::getUserId).filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
    }

    private String getUserId(String uuid) {
        UserVo userVo = userMapper.getUserByUuid(uuid);
        return userVo == null ? uuid : userVo.getUserId();
    }

    private Long parseLong(String value, int rowNum, String fieldName) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            if (value.endsWith(".0")) {
                value = value.substring(0, value.length() - 2);
            }
            return Long.parseLong(value);
        } catch (Exception ex) {
            throw new IssueImportException(rowNum, fieldName + "必须是整数");
        }
    }

    private Integer parseInteger(String value, int rowNum, String fieldName) {
        try {
            if (value.endsWith(".0")) {
                value = value.substring(0, value.length() - 2);
            }
            return Integer.parseInt(value);
        } catch (Exception ex) {
            throw new IssueImportException(rowNum, fieldName + "必须是整数");
        }
    }

    private String truncateCellValue(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        return value.length() > 32767 ? value.substring(0, 32767) : value;
    }

    private class ImportReferenceData {
        private final AppVo appVo;
        private final Map<String, Long> statusMap = new HashMap<>();
        private final Map<String, Long> priorityMap = new HashMap<>();
        private final Map<String, Long> iterationMap = new HashMap<>();
        private final Map<String, Long> catalogMap = new HashMap<>();

        private ImportReferenceData(AppVo appVo) {
            this.appVo = appVo;
            initStatusMap(appVo);
            initPriorityMap();
            initIterationMap(appVo.getProjectId());
            initCatalogMap(appVo.getId());
        }

        private Long getStartStatusId() {
            if (CollectionUtils.isNotEmpty(appVo.getStatusList())) {
                Optional<AppStatusVo> startStatus = appVo.getStatusList().stream().filter(status -> Integer.valueOf(1).equals(status.getIsStart())).findFirst();
                if (startStatus.isPresent()) {
                    return startStatus.get().getId();
                }
            }
            return null;
        }

        private Long getStatusId(String value, int rowNum) {
            Long statusId = statusMap.get(value);
            if (statusId == null) {
                throw new IssueImportException(rowNum, "状态“" + value + "”不存在");
            }
            return statusId;
        }

        private Long getPriorityId(String value, int rowNum) {
            Long priorityId = priorityMap.get(value);
            if (priorityId == null) {
                throw new IssueImportException(rowNum, "优先级“" + value + "”不存在");
            }
            return priorityId;
        }

        private Long getIterationId(String value, int rowNum) {
            Long iterationId = iterationMap.get(value);
            if (iterationId == null) {
                throw new IssueImportException(rowNum, "迭代“" + value + "”不存在");
            }
            return iterationId;
        }

        private Long getCatalogId(String value, int rowNum) {
            Long catalogId = catalogMap.get(value);
            if (catalogId == null) {
                throw new IssueImportException(rowNum, "目录“" + value + "”不存在");
            }
            return catalogId;
        }

        private void initStatusMap(AppVo appVo) {
            if (CollectionUtils.isNotEmpty(appVo.getStatusList())) {
                for (AppStatusVo statusVo : appVo.getStatusList()) {
                    statusMap.put(statusVo.getName(), statusVo.getId());
                    statusMap.put(statusVo.getLabel(), statusVo.getId());
                }
            }
        }

        private void initPriorityMap() {
            List<PriorityVo> priorityList = priorityMapper.getPriorityList();
            if (CollectionUtils.isNotEmpty(priorityList)) {
                for (PriorityVo priorityVo : priorityList) {
                    priorityMap.put(priorityVo.getName(), priorityVo.getId());
                }
            }
        }

        private void initIterationMap(Long projectId) {
            IterationVo conditionVo = new IterationVo();
            conditionVo.setProjectId(projectId);
            int count = iterationMapper.searchIterationCount(conditionVo);
            if (count > 0) {
                conditionVo.setMaxPageSize(Math.max(count, 20));
                conditionVo.setPageSize(count);
                List<IterationVo> iterationList = iterationMapper.searchIteration(conditionVo);
                if (CollectionUtils.isNotEmpty(iterationList)) {
                    for (IterationVo iterationVo : iterationList) {
                        iterationMap.put(iterationVo.getName(), iterationVo.getId());
                    }
                }
            }
        }

        private void initCatalogMap(Long appId) {
            AppCatalogVo conditionVo = new AppCatalogVo();
            conditionVo.setAppId(appId);
            List<AppCatalogVo> catalogList = catalogMapper.searchAppCatalog(conditionVo);
            if (CollectionUtils.isEmpty(catalogList)) {
                return;
            }
            Map<Long, AppCatalogVo> catalogIdMap = catalogList.stream().collect(Collectors.toMap(AppCatalogVo::getId, catalog -> catalog, (a, b) -> a));
            for (AppCatalogVo catalogVo : catalogList) {
                catalogMap.put(catalogVo.getName(), catalogVo.getId());
                catalogMap.put(getCatalogFullPath(catalogVo, catalogIdMap), catalogVo.getId());
            }
        }

        private String getCatalogFullPath(AppCatalogVo catalogVo, Map<Long, AppCatalogVo> catalogIdMap) {
            LinkedList<String> pathList = new LinkedList<>();
            AppCatalogVo current = catalogVo;
            while (current != null) {
                pathList.addFirst(current.getName());
                Long parentId = current.getParentId();
                current = parentId == null || parentId == 0L ? null : catalogIdMap.get(parentId);
            }
            return String.join("/", pathList);
        }
    }

    private static class ImportRow {
        private IssueVo issueVo;
        private boolean noChange = false;
        private final JSONObject resultObj = new JSONObject();

        private ImportRow(int rowNum) {
            resultObj.put("rowNum", rowNum);
            resultObj.put("status", "success");
            resultObj.put("errorList", new JSONArray());
        }

        public IssueVo getIssueVo() {
            return issueVo;
        }

        public void setIssueVo(IssueVo issueVo) {
            this.issueVo = issueVo;
        }

        public String getStatus() {
            return resultObj.getString("status");
        }

        public void setStatus(String status) {
            resultObj.put("status", status);
        }

        public JSONObject getResultObj() {
            return resultObj;
        }

        public boolean getNoChange() {
            return noChange;
        }

        public void setNoChange(boolean noChange) {
            this.noChange = noChange;
        }

        public void addError(String error) {
            setStatus("failed");
            resultObj.getJSONArray("errorList").add(error);
        }
    }

    private static class ExcelColumn {
        private final String key;
        private final String label;
        private final String help;
        private AppAttrVo attr;

        private ExcelColumn(String key, String label, String help) {
            this.key = key;
            this.label = label;
            this.help = help;
        }

        private ExcelColumn(String key, String label, String help, AppAttrVo attr) {
            this(key, label, help);
            this.attr = attr;
        }

        public String getKey() {
            return key;
        }

        public String getLabel() {
            return label;
        }

        public String getHelp() {
            return help;
        }

        public AppAttrVo getAttr() {
            return attr;
        }
    }
}
