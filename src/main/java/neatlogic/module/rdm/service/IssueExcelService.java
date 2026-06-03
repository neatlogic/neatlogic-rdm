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

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IssueExcelService {

    void exportIssue(JSONObject paramObj, HttpServletResponse response) throws Exception;

    void downloadImportTemplate(Long appId, HttpServletResponse response) throws Exception;

    JSONObject previewImportIssue(Long appId, MultipartFile multipartFile) throws Exception;

    JSONObject importIssue(Long appId, MultipartFile multipartFile) throws Exception;
}
