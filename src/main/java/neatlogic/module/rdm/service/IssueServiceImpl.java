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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.exception.user.UserNotFoundException;
import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerFactory;
import neatlogic.framework.fulltextindex.core.IFullTextIndexHandler;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.IssueFullTextIndexType;
import neatlogic.framework.rdm.enums.IssueRelType;
import neatlogic.framework.rdm.exception.IssueNotFoundException;
import neatlogic.framework.util.Md5Util;
import neatlogic.module.rdm.dao.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private AttrMapper attrMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AppMapper appMapper;

    @Override
    public IssueVo getIssueByIdForAudit(Long id) {
        IssueVo issueVo = issueMapper.getIssueByIdForAudit(id);
        makeupIssue(issueVo);
        return issueVo;
    }

    @Override
    public void saveIssue(IssueVo issueVo) {
        if (issueVo.getIsNew()) {
            issueMapper.insertIssue(issueVo);
        } else {
            issueMapper.updateIssue(issueVo);
            issueMapper.deleteIssueTagByIssueId(issueVo.getId());
            issueMapper.deleteIssueUserByIssueId(issueVo.getId());
        }
        if (CollectionUtils.isNotEmpty(issueVo.getAttrList())) {
            issueMapper.replaceIssueAttr(issueVo);
        }
        if (CollectionUtils.isNotEmpty(issueVo.getTagList())) {
            for (String tag : issueVo.getTagList()) {
                TagVo tagVo = tagMapper.getTagByName(tag);
                if (tagVo == null) {
                    tagVo = new TagVo(tag);
                    tagMapper.insertTag(tagVo);
                }
                issueMapper.insertIssueTag(issueVo.getId(), tagVo.getId());
            }
        }

        if (CollectionUtils.isNotEmpty(issueVo.getUserIdList())) {
            for (String userId : issueVo.getUserIdList()) {
                userId = userId.replace(GroupSearch.USER.getValuePlugin(), "");
                if (!Md5Util.isMd5(userId)) {
                    UserVo userVo = userMapper.getUserByUserId(userId);
                    if (userVo != null) {
                        userId = userVo.getUuid();
                    } else {
                        throw new UserNotFoundException(userId);
                    }
                }
                issueMapper.insertIssueUser(issueVo.getId(), userId);
            }
        }

        if (StringUtils.isNotBlank(issueVo.getComment())) {
            CommentVo commentVo = new CommentVo();
            commentVo.setIssueId(issueVo.getId());
            commentVo.setContent(issueVo.getComment());
            commentVo.setFcu(UserContext.get().getUserUuid(true));
            commentVo.setStatus(issueVo.getStatus());
            commentMapper.insertComment(commentVo);
        }
        //创建来源关系
        if (issueVo.getFromId() != null) {
            IssueVo fromIssue = issueMapper.getIssueById(issueVo.getFromId());
            if (fromIssue != null) {
                IssueRelVo issueRelVo = new IssueRelVo(fromIssue.getAppId(), fromIssue.getId(), issueVo.getAppId(), issueVo.getId(), StringUtils.isNotBlank(issueVo.getRelType()) ? issueVo.getRelType() : IssueRelType.EXTEND.getValue());
                issueMapper.insertIssueRel(issueRelVo);
            }
        } else if (issueVo.getToId() != null) {
            IssueVo toIssue = issueMapper.getIssueById(issueVo.getToId());
            if (toIssue != null) {
                IssueRelVo issueRelVo = new IssueRelVo(issueVo.getAppId(), issueVo.getId(), toIssue.getAppId(), toIssue.getId(), StringUtils.isNotBlank(issueVo.getRelType()) ? issueVo.getRelType() : IssueRelType.EXTEND.getValue());
                issueMapper.insertIssueRel(issueRelVo);
            }
        }

        //创建全文检索索引
        IFullTextIndexHandler indexHandler = FullTextIndexHandlerFactory.getHandler(IssueFullTextIndexType.ISSUE);
        if (indexHandler != null) {
            indexHandler.createIndex(issueVo.getId());
        }
    }

    @Override
    public IssueVo copyIssue(Long sourceIssueId) {
        IssueVo sourceIssue = getIssueById(sourceIssueId);
        if (sourceIssue == null) {
            throw new IssueNotFoundException(sourceIssueId);
        }
        if (sourceIssue.getSourceIssueId() != null) {
            sourceIssue = getIssueById(sourceIssue.getSourceIssueId());
            if (sourceIssue == null) {
                throw new IssueNotFoundException(sourceIssueId);
            }
        }
        IssueVo copyIssue = new IssueVo();
        Long copyIssueId = copyIssue.getId();
        copyIssue.setSourceIssueId(sourceIssue.getId());
        copyIssue.setAppId(sourceIssue.getAppId());
        copyIssue.setName(sourceIssue.getName());
        copyIssue.setCreateUser(UserContext.get().getUserUuid(true));
        copyIssue.setPriority(sourceIssue.getPriority());
        copyIssue.setIteration(sourceIssue.getIteration());
        copyIssue.setCatalog(sourceIssue.getCatalog());
        copyIssue.setStartDate(sourceIssue.getStartDate());
        copyIssue.setEndDate(sourceIssue.getEndDate());
        copyIssue.setTimecost(sourceIssue.getTimecost());
        copyIssue.setContent(sourceIssue.getContent());
        copyIssue.setTagList(sourceIssue.getTagList());
        copyIssue.setUserIdList(sourceIssue.getUserIdList());
        copyIssue.setStatus(getCopyStatus(sourceIssue));
        if (CollectionUtils.isNotEmpty(sourceIssue.getAttrList())) {
            List<IssueAttrVo> attrList = new ArrayList<>();
            for (IssueAttrVo sourceAttr : sourceIssue.getAttrList()) {
                IssueAttrVo copyAttr = new IssueAttrVo(sourceAttr.getAttrId(), copyIssueId, sourceAttr.getAttrType(), sourceAttr.getConfig());
                if (CollectionUtils.isNotEmpty(sourceAttr.getValueList())) {
                    copyAttr.setValueList(JSON.parseArray(JSON.toJSONString(sourceAttr.getValueList())));
                }
                attrList.add(copyAttr);
            }
            copyIssue.setAttrList(attrList);
        }
        saveIssue(copyIssue);
        if (CollectionUtils.isNotEmpty(sourceIssue.getFileList())) {
            for (FileVo fileVo : sourceIssue.getFileList()) {
                issueMapper.insertIssueFile(copyIssue.getId(), fileVo.getId());
            }
        }
        return copyIssue;
    }

    private Long getCopyStatus(IssueVo sourceIssue) {
        IssueVo statusCondition = new IssueVo();
        statusCondition.setAppId(sourceIssue.getAppId());
        List<AppStatusVo> statusList = appMapper.getStatusByAppId(statusCondition);
        if (CollectionUtils.isNotEmpty(statusList)) {
            Optional<AppStatusVo> startStatus = statusList.stream().filter(status -> Integer.valueOf(1).equals(status.getIsStart())).findFirst();
            if (startStatus.isPresent()) {
                return startStatus.get().getId();
            }
        }
        return sourceIssue.getStatus();
    }

    @Override
    public IssueVo getIssueById(Long id) {
        IssueVo issueVo = issueMapper.getIssueById(id);
        makeupIssue(issueVo);
        return issueVo;
    }

    private void makeupIssue(IssueVo issueVo) {
        if (issueVo != null) {
            ProjectVo projectVo = projectMapper.getProjectById(issueVo.getProjectId());
            issueVo.setIsProjectLeader(projectVo.getIsLeader());
            issueVo.setIsProjectMember(projectVo.getIsMember());
            issueVo.setIsProjectOwner(projectVo.getIsOwner());
            List<AppAttrVo> attrList = attrMapper.getAttrByAppId(issueVo.getAppId());
            for (AppAttrVo attr : attrList) {
                if (attr.getIsPrivate().equals(0)) {
                    issueVo.addAppAttr(attr);
                }
            }
            List<Long> idList = new ArrayList<>();
            idList.add(issueVo.getId());
            issueVo.setIdList(idList);
            HashMap<String, ?> attrMap = issueMapper.getAttrByIssueId(issueVo);
            if (MapUtils.isNotEmpty(attrMap)) {
                List<IssueAttrVo> issueAttrList = new ArrayList<>();
                for (String key : attrMap.keySet()) {
                    if (!key.equals("issueId")) {
                        Long attrId = Long.parseLong(key);
                        Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getId().equals(attrId)).findFirst();
                        if (op.isPresent()) {
                            AppAttrVo appAttrVo = op.get();
                            IssueAttrVo issueAttrVo = new IssueAttrVo();
                            issueAttrVo.setIssueId(issueVo.getId());
                            issueAttrVo.setAttrId(attrId);
                            issueAttrVo.setAttrType(appAttrVo.getType());
                            issueAttrVo.setConfig(appAttrVo.getConfig());
                            if (attrMap.get(key).toString().startsWith("[") && attrMap.get(key).toString().endsWith("]")) {
                                JSONArray valueList = JSON.parseArray(attrMap.get(key).toString());
                                issueAttrVo.setValueList(valueList);
                            } else {
                                issueAttrVo.setValueList(new JSONArray() {{
                                    this.add(attrMap.get(key));
                                }});
                            }
                            issueAttrList.add(issueAttrVo);
                        }
                    }
                }
                issueVo.setAttrList(issueAttrList);
            }
        }
    }


}
