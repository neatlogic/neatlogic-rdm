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
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.enums.IssueRelDirection;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiffIssue {
    public static List<IssueAuditVo> getDiff(Long issueId, IssueVo oldIssueVo, IssueVo newIssueVo, List<AppAttrVo> attrList) {
        List<IssueAuditVo> auditList = new ArrayList<>();
        //比较名称
        if (!Objects.equals(oldIssueVo.getName(), newIssueVo.getName())) {
            auditList.add(new IssueAuditVo(issueId, "name", oldIssueVo.getName(), newIssueVo.getName()));
        }
        //比较内容
        if (!Objects.equals(oldIssueVo.getContent(), newIssueVo.getContent())) {
            auditList.add(new IssueAuditVo(issueId, "content", oldIssueVo.getContent(), newIssueVo.getContent()));
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
            auditList.add(new IssueAuditVo(issueId, "file", oldFileObjList, newFileObjList));
        }
        //比较自定义属性
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
        //比较关系
        if (!Objects.equals(oldIssueVo.getIssueRelList(), newIssueVo.getIssueRelList())) {
            List<String> oldAppTypeList = oldIssueVo.getIssueRelList().stream().map(d -> d.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? d.getToAppType() : d.getFromAppType()).distinct().collect(Collectors.toList());
            List<String> newAppTypeList = newIssueVo.getIssueRelList().stream().map(d -> d.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? d.getToAppType() : d.getFromAppType()).distinct().collect(Collectors.toList());
            List<String> appTypeList = Stream.concat(oldAppTypeList.stream(), newAppTypeList.stream())
                    .distinct()
                    .collect(Collectors.toList());
            for (String appType : appTypeList) {
                JSONArray oldRelList = new JSONArray();
                for (IssueRelVo f : oldIssueVo.getIssueRelList().stream().filter(d -> d.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? d.getToAppType().equalsIgnoreCase(appType) : d.getFromAppType().equalsIgnoreCase(appType)).collect(Collectors.toList())) {
                    oldRelList.add(new JSONObject() {{
                        this.put("id", f.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? f.getToIssueId() : f.getFromIssueId());
                        this.put("appId", f.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? f.getToAppId() : f.getFromAppId());
                        this.put("appType", f.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? f.getToAppType() : f.getFromAppType());
                        this.put("name", f.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? f.getToIssueName() : f.getFromIssueName());
                    }});
                }
                JSONArray newRelList = new JSONArray();
                for (IssueRelVo f : newIssueVo.getIssueRelList().stream().filter(d -> d.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? d.getToAppType().equalsIgnoreCase(appType) : d.getFromAppType().equalsIgnoreCase(appType)).collect(Collectors.toList())) {
                    newRelList.add(new JSONObject() {{
                        this.put("id", f.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? f.getToIssueId() : f.getFromIssueId());
                        this.put("appId", f.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? f.getToAppId() : f.getFromAppId());
                        this.put("appType", f.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? f.getToAppType() : f.getFromAppType());
                        this.put("name", f.getDirection().equalsIgnoreCase(IssueRelDirection.FROM.getValue()) ? f.getToIssueName() : f.getFromIssueName());
                    }});
                }
                auditList.add(new IssueAuditVo(issueId, "rel_" + appType, oldRelList, newRelList));
            }
        }
        return auditList;
    }

}
