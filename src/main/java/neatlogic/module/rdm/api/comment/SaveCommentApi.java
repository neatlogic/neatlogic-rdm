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

package neatlogic.module.rdm.api.comment;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.CommentVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.exception.IssueNotFoundException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.CommentMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
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
    private IssueMapper issueMapper;


    @Override
    public String getName() {
        return "保存评论";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "issueId", type = ApiParamType.LONG, isRequired = true, desc = "任务id"),
            @Param(name = "content", type = ApiParamType.STRING, desc = "内容", isRequired = true),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "回复评论id")
    })
    @Output({@Param(type = ApiParamType.LONG, desc = "真正的父评论id")})
    @Description(desc = "保存评论接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        CommentVo commentVo = JSONObject.toJavaObject(paramObj, CommentVo.class);
        if (commentVo.getParentId() != null) {
            CommentVo parentCommentVo = commentMapper.getCommentById(commentVo.getParentId());
            if (parentCommentVo != null && parentCommentVo.getParentId() != null) {
                commentVo.setParentId(parentCommentVo.getParentId());
            }
        }
        IssueVo issueVo = issueMapper.getIssueById(commentVo.getIssueId());
        if (issueVo == null) {
            throw new IssueNotFoundException(commentVo.getIssueId());
        }
        commentVo.setStatus(issueVo.getStatus());
        commentVo.setFcu(UserContext.get().getUserUuid(true));
        commentMapper.insertComment(commentVo);
        return commentVo.getParentId();
    }

    @Override
    public String getToken() {
        return "/rdm/issue/comment/save";
    }
}
