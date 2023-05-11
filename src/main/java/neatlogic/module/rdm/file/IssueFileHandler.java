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

package neatlogic.module.rdm.file;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.file.core.FileTypeHandlerBase;
import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.rdm.dto.IssueAuditVo;
import neatlogic.module.rdm.dao.mapper.IssueAuditMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IssueFileHandler extends FileTypeHandlerBase {
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private IssueAuditMapper issueAuditMapper;

    @Override
    public boolean valid(String userUuid, FileVo fileVo, JSONObject jsonObj) {
        return true;
    }

    @Override
    public boolean validDeleteFile(FileVo fileVo) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return "任务管理附件";
    }

    @Override
    protected boolean myDeleteFile(FileVo fileVo, JSONObject paramObj) {
        Long issueId = issueMapper.getIssueIdByFileId(fileVo.getId());
        if (issueId != null) {
            List<FileVo> oldFileList = issueMapper.getIssueFileByIssueId(issueId);
            List<FileVo> newFileList = oldFileList.stream().filter(d -> !d.getId().equals(fileVo.getId())).collect(Collectors.toList());
            List<String> oldFileNameList = oldFileList.stream().map(FileVo::getName).collect(Collectors.toList());
            List<String> newFileNameList = newFileList.stream().map(FileVo::getName).collect(Collectors.toList());
            issueAuditMapper.insertIssueAudit(new IssueAuditVo(issueId, "file", oldFileNameList, newFileNameList));
        }
        issueMapper.deleteIssueFileByFileId(fileVo.getId());
        return true;
    }

    @Override
    public void afterUpload(FileVo fileVo, JSONObject jsonObj) {
        Long issueId = jsonObj.getLong("issueId");
        List<FileVo> oldFileList = issueMapper.getIssueFileByIssueId(issueId);
        List<String> oldFileNameList = oldFileList.stream().map(FileVo::getName).collect(Collectors.toList());
        issueMapper.insertIssueFile(issueId, fileVo.getId());
        List<FileVo> newFileList = issueMapper.getIssueFileByIssueId(issueId);
        List<String> newFileNameList = newFileList.stream().map(FileVo::getName).collect(Collectors.toList());
        issueAuditMapper.insertIssueAudit(new IssueAuditVo(issueId, "file", oldFileNameList, newFileNameList));
    }

    @Override
    public String getName() {
        return "ISSUE";
    }

}
