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

package neatlogic.module.rdm.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.IssueAttrVo;
import neatlogic.framework.rdm.dto.IssueAuditVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.AttrType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DiffIssue {
    public static List<IssueAuditVo> getDiff(Long issueId, IssueVo oldIssueVo, IssueVo newIssueVo, List<AppAttrVo> attrList) {
        List<IssueAuditVo> auditList = new ArrayList<>();
        //比较名称
        if (!Objects.equals(oldIssueVo.getName(), newIssueVo.getName())) {
            auditList.add(new IssueAuditVo(issueId, "name", oldIssueVo.getName(), newIssueVo.getName()));
        }
        //比较优先级
        if (!Objects.equals(oldIssueVo.getPriority(), newIssueVo.getPriority())) {
            Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getType().equalsIgnoreCase(AttrType.PRIORITY.getType())).findFirst();
            op.ifPresent(appAttrVo -> auditList.add(new IssueAuditVo(issueId, appAttrVo.getId(), oldIssueVo.getPriority(), newIssueVo.getPriority())));
        }
        //比较目录
        if (!Objects.equals(oldIssueVo.getCatalog(), newIssueVo.getCatalog())) {
            Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getType().equalsIgnoreCase(AttrType.CATALOG.getType())).findFirst();
            op.ifPresent(appAttrVo -> auditList.add(new IssueAuditVo(issueId, appAttrVo.getId(), oldIssueVo.getCatalog(), newIssueVo.getCatalog())));
        }
        //比较迭代
        if (!Objects.equals(oldIssueVo.getIteration(), newIssueVo.getIteration())) {
            Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getType().equalsIgnoreCase(AttrType.ITERATION.getType())).findFirst();
            op.ifPresent(appAttrVo -> auditList.add(new IssueAuditVo(issueId, appAttrVo.getId(), oldIssueVo.getIteration(), newIssueVo.getIteration())));
        }
        //比较处理人
        if (!CollectionUtils.isEqualCollection(oldIssueVo.getUserIdList(), newIssueVo.getUserIdList())) {
            Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getType().equalsIgnoreCase(AttrType.WORKER.getType())).findFirst();
            op.ifPresent(appAttrVo -> auditList.add(new IssueAuditVo(issueId, appAttrVo.getId(), CollectionUtils.isNotEmpty(oldIssueVo.getUserIdList()) ? oldIssueVo.getUserIdList() : null,
                    CollectionUtils.isNotEmpty(newIssueVo.getUserIdList()) ? newIssueVo.getUserIdList() : null)));
        }
        //比较预计开始
        if (!Objects.equals(oldIssueVo.getStartDate(), newIssueVo.getStartDate())) {
            Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getType().equalsIgnoreCase(AttrType.STARTDATE.getType())).findFirst();
            op.ifPresent(appAttrVo -> auditList.add(new IssueAuditVo(issueId, appAttrVo.getId(), oldIssueVo.getStartDate(), newIssueVo.getStartDate())));
        }
        //比较预计结束
        if (!Objects.equals(oldIssueVo.getEndDate(), newIssueVo.getEndDate())) {
            Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getType().equalsIgnoreCase(AttrType.ENDDATE.getType())).findFirst();
            op.ifPresent(appAttrVo -> auditList.add(new IssueAuditVo(issueId, appAttrVo.getId(), oldIssueVo.getEndDate(), newIssueVo.getEndDate())));
        }
        //比较标签
        if (!CollectionUtils.isEqualCollection(oldIssueVo.getTagList(), newIssueVo.getTagList())) {
            Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getType().equalsIgnoreCase(AttrType.TAG.getType())).findFirst();
            op.ifPresent(appAttrVo -> auditList.add(new IssueAuditVo(issueId, appAttrVo.getId(), new JSONArray() {{
                this.addAll(oldIssueVo.getTagList());
            }}, new JSONArray() {{
                this.addAll(newIssueVo.getTagList());
            }})));
        }
        //比较状态
        if (!Objects.equals(oldIssueVo.getStatus(), newIssueVo.getStatus())) {
            auditList.add(new IssueAuditVo(issueId, "status", oldIssueVo.getStatus(), newIssueVo.getStatus()));
        }
        //比较附件
        if (!Objects.equals(oldIssueVo.getFileIdList(), newIssueVo.getFileIdList())) {
            JSONArray oldFileObjList = new JSONArray();
            for (FileVo f : oldIssueVo.getFileList()) {
                oldFileObjList.add(new JSONObject() {{
                    this.put("id", f.getId());
                    this.put("name", f.getName());
                }});
            }
            JSONArray newFileObjList = new JSONArray();
            for (FileVo f : newIssueVo.getFileList()) {
                newFileObjList.add(new JSONObject() {{
                    this.put("id", f.getId());
                    this.put("name", f.getName());
                }});
            }
            auditList.add(new IssueAuditVo(issueId, "file", CollectionUtils.isNotEmpty(oldFileObjList) ? oldFileObjList : null, CollectionUtils.isNotEmpty(newFileObjList) ? newFileObjList : null));
        }

        if (CollectionUtils.isNotEmpty(attrList)) {
            for (AppAttrVo attrVo : attrList) {
                if (attrVo.getIsPrivate().equals(0)) {
                    IssueAttrVo oldAttrVo = oldIssueVo.getAttr(attrVo.getId());
                    boolean hasOldValue = false;
                    boolean hasNewValue = false;
                    if (oldAttrVo != null) {
                        oldAttrVo.setAttrType(attrVo.getType());
                        if (CollectionUtils.isNotEmpty(oldAttrVo.getValueList())) {
                            hasOldValue = true;
                        }
                    }
                    IssueAttrVo newAttrVo = newIssueVo.getAttr(attrVo.getId());
                    if (newAttrVo != null) {
                        newAttrVo.setAttrType(attrVo.getType());
                        if (CollectionUtils.isNotEmpty(newAttrVo.getValueList())) {
                            hasNewValue = true;
                        }
                    }
                    if ((hasOldValue || hasNewValue) && !Objects.equals(oldAttrVo, newAttrVo)) {
                        auditList.add(new IssueAuditVo(issueId, attrVo.getId(), (oldAttrVo != null ? oldAttrVo.getValueList() : null), (newAttrVo != null ? newAttrVo.getValueList() : null)));
                    }
                }
            }
        }
        return auditList;
    }

}
