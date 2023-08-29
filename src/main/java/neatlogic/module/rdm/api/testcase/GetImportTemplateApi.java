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

package neatlogic.module.rdm.api.testcase;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateBinaryStreamApiComponentBase;
import neatlogic.framework.util.excel.ExcelBuilder;
import neatlogic.framework.util.excel.SheetBuilder;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetImportTemplateApi extends PrivateBinaryStreamApiComponentBase {

    static Logger logger = LoggerFactory.getLogger(GetImportTemplateApi.class);

    private final int validationOptionSize = 100;// 限制数据有效性列表长度

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AttrMapper attrMapper;

    @Override
    public String getToken() {
        return "/rdm/testcase/template/get";
    }

    @Override
    public String getName() {
        return "下载测试用例导入模板";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.projectid"),
            @Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "nmraa.getappapi.input.param.desc")
    })
    @Output({})
    @Description(desc = "下载测试用例导入模板")
    @Override
    public Object myDoService(JSONObject paramObj, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long projectId = paramObj.getLong("projectId");
        Long appId = paramObj.getLong("appId");
        ProjectVo projectVo = projectMapper.getProjectById(projectId);
        List<AppAttrVo> attrList = attrMapper.getAttrByAppId(appId);
        String fileName = projectVo.getName() + "TestcaseImportTemplate.xlsx";
        List<String> columnList = new ArrayList<String>() {{
            this.add("id");
            this.add("name");
            this.add("condition");
            this.add("step");
            this.add("result");
        }};
        List<String> systemAttrList = new ArrayList<String>() {{
            this.add("id");
            this.add("用例名称");
            this.add("前置条件");
            this.add("用例步骤");
            this.add("预期结果");
        }};
        for (AppAttrVo attrVo : attrList) {
            systemAttrList.add(attrVo.getLabel());
            columnList.add("attr_" + attrVo.getName());
        }
        ExcelBuilder builder = new ExcelBuilder(SXSSFWorkbook.class);
        SheetBuilder sheetBuilder = builder.withBorderColor(HSSFColor.HSSFColorPredefined.GREY_40_PERCENT)
                .withHeadFontColor(HSSFColor.HSSFColorPredefined.WHITE)
                .withHeadBgColor(HSSFColor.HSSFColorPredefined.DARK_BLUE)
                .withColumnWidth(30)
                .addSheet("数据")
                .withHeaderList(systemAttrList).withColumnList(columnList);

        Workbook workbook = builder.build();
        Map<String, Object> demoData = new HashMap<>();
        demoData.put("id", "提供id代表更新，不提供id代表新增");
        demoData.put("name", "用例名称");
        demoData.put("condition", "请输入测试用例的前置条件");
        demoData.put("step", "请输入测试用例的测试步骤");
        demoData.put("result", "请输入测试用例的预期结果");
        for (AppAttrVo attrVo : attrList) {
            if (attrVo.getAllowImport()) {
                demoData.put("attr_" + attrVo.getName(), attrVo.getImportHelp());
            }
        }
        sheetBuilder.addData(demoData);

        boolean flag = request.getHeader("User-Agent").indexOf("Gecko") > 0;
        if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0 || flag) {
            fileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
        } else {
            fileName = new String(fileName.replace(" ", "").getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", " attachment; filename=\"" + fileName + "\"");
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
            workbook.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 设置数据有效性
     * 数据有效性列表过长，总字符数超过255时，需要将列表放在单独的sheet
     *
     * @param workbook       需要设置数据有效性的workbook
     * @param targetSheet    需要设置需要设置数据有效性的sheet
     * @param sheetName      数据有效性列表sheet名称
     * @param sheetNameIndex 数据有效性列表sheet序号
     * @param sheetData      数据有效性列表
     * @param firstRow       数据有效性开始行
     * @param lastRow        数据有效性结束行
     * @param firstCol       数据有效性开始列
     * @param lastCol        数据有效性结束列
     */
    private void addValidationData(Workbook workbook, Sheet targetSheet, String sheetName, int sheetNameIndex, String[] sheetData, int firstRow, int lastRow, int firstCol, int lastCol) {
        Sheet hidden = workbook.createSheet(sheetName);
        Cell cell;
        for (int i = 0, length = sheetData.length; i < length; i++) {
            String name = sheetData[i];
            Row row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name);
        }
        Name namedCell = workbook.createName();
        namedCell.setNameName(sheetName);
        namedCell.setRefersToFormula(sheetName + "!$A$1:$A$" + sheetData.length);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(sheetName);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        dataValidation.setSuppressDropDownArrow(false);// false时显示下拉箭头(office2007)
        dataValidation.setShowErrorBox(true);
        workbook.setSheetHidden(sheetNameIndex, true);
        targetSheet.addValidationData(dataValidation);
    }
}
