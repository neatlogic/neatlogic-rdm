/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.module.rdm.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.file.core.FileTypeHandlerBase;
import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.rdm.dto.IssueAuditVo;
import neatlogic.module.rdm.dao.mapper.IssueAuditMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.apache.commons.collections4.CollectionUtils;
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
            JSONArray oldFileObjList = new JSONArray();
            for (FileVo f : oldFileList) {
                oldFileObjList.add(new JSONObject() {{
                    this.put("id", f.getId());
                    this.put("name", f.getName());
                }});
            }
            JSONArray newFileObjList = new JSONArray();
            for (FileVo f : newFileList) {
                newFileObjList.add(new JSONObject() {{
                    this.put("id", f.getId());
                    this.put("name", f.getName());
                }});
            }
            issueAuditMapper.insertIssueAudit(new IssueAuditVo(issueId, "file", CollectionUtils.isNotEmpty(oldFileObjList) ? oldFileObjList : null, CollectionUtils.isNotEmpty(newFileObjList) ? newFileObjList : null));
        }
        issueMapper.deleteIssueFileByFileId(fileVo.getId());
        return true;
    }

    @Override
    public void afterUpload(FileVo fileVo, JSONObject jsonObj) {
        Long issueId = jsonObj.getLong("issueId");
        List<FileVo> oldFileList = issueMapper.getIssueFileByIssueId(issueId);
        JSONArray oldFileObjList = new JSONArray();
        for (FileVo f : oldFileList) {
            oldFileObjList.add(new JSONObject() {{
                this.put("id", f.getId());
                this.put("name", f.getName());
            }});
        }
        issueMapper.insertIssueFile(issueId, fileVo.getId());
        List<FileVo> newFileList = issueMapper.getIssueFileByIssueId(issueId);
        JSONArray newFileObjList = new JSONArray();
        for (FileVo f : newFileList) {
            newFileObjList.add(new JSONObject() {{
                this.put("id", f.getId());
                this.put("name", f.getName());
            }});
        }
        issueAuditMapper.insertIssueAudit(new IssueAuditVo(issueId, "file", CollectionUtils.isNotEmpty(oldFileObjList) ? oldFileObjList : null, CollectionUtils.isNotEmpty(newFileObjList) ? newFileObjList : null));
    }

    @Override
    public String getName() {
        return "ISSUE";
    }

}
