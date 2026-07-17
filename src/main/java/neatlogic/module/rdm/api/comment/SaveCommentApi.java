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

package neatlogic.module.rdm.api.comment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.CommentVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.exception.IssueNotFoundException;
import neatlogic.framework.rdm.notify.constvalue.RdmIssueNotifyTriggerType;
import neatlogic.framework.rdm.notify.dto.RdmNotifyContextVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.CommentMapper;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.notify.service.RdmNotifyService;
import neatlogic.module.rdm.service.IssueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.CREATE)
@Transactional
public class SaveCommentApi extends PrivateApiComponentBase {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private RdmNotifyService rdmNotifyService;

    @Resource
    private IssueService issueService;


    @Override
    public String getName() {
        return "保存评论";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "评论id，不提供代表添加"),
            @Param(name = "issueId", type = ApiParamType.LONG, isRequired = true, desc = "任务id"),
            @Param(name = "content", type = ApiParamType.STRING, desc = "内容", isRequired = true),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "回复评论id")
    })
    @Output({@Param(type = ApiParamType.LONG, desc = "真正的父评论id")})
    @Description(desc = "保存评论")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        CommentVo commentVo = JSON.toJavaObject(paramObj, CommentVo.class);
        IssueVo issueVo = issueService.getIssueById(commentVo.getIssueId());
        if (issueVo == null) {
            throw new IssueNotFoundException(commentVo.getIssueId());
        }
        commentVo.setStatus(issueVo.getStatus());
        if (id == null) {
            CommentVo repliedCommentVo = null;
            if (commentVo.getParentId() != null) {
                CommentVo parentCommentVo = commentMapper.getCommentById(commentVo.getParentId());
                repliedCommentVo = parentCommentVo;
                if (parentCommentVo != null && parentCommentVo.getParentId() != null) {
                    commentVo.setParentId(parentCommentVo.getParentId());
                }
            }
            commentVo.setFcu(UserContext.get().getUserUuid(true));
            commentMapper.insertComment(commentVo);
            RdmNotifyContextVo contextVo = new RdmNotifyContextVo();
            contextVo.setBizType(appMapper.getAppById(issueVo.getAppId()).getType());
            contextVo.setIssueVo(issueVo);
            contextVo.setCommentVo(commentVo);
            if (repliedCommentVo == null) {
                rdmNotifyService.notify(contextVo, RdmIssueNotifyTriggerType.COMMENTED);
            } else {
                contextVo.setReplyUserUuid(repliedCommentVo.getFcu());
                rdmNotifyService.notify(contextVo, RdmIssueNotifyTriggerType.REPLIED);
            }
        } else {
            commentMapper.updateComment(commentVo);
        }
        return commentVo.getParentId();
    }

    @Override
    public String getToken() {
        return "/rdm/issue/comment/save";
    }
}
