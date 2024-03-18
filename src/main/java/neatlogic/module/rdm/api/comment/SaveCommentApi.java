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

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "评论id，不提供代表添加"),
            @Param(name = "issueId", type = ApiParamType.LONG, isRequired = true, desc = "任务id"),
            @Param(name = "content", type = ApiParamType.STRING, desc = "内容", isRequired = true),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "回复评论id")
    })
    @Output({@Param(type = ApiParamType.LONG, desc = "真正的父评论id")})
    @Description(desc = "保存评论接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        CommentVo commentVo = JSONObject.toJavaObject(paramObj, CommentVo.class);
        IssueVo issueVo = issueMapper.getIssueById(commentVo.getIssueId());
        if (issueVo == null) {
            throw new IssueNotFoundException(commentVo.getIssueId());
        }
        commentVo.setStatus(issueVo.getStatus());
        if (id == null) {
            if (commentVo.getParentId() != null) {
                CommentVo parentCommentVo = commentMapper.getCommentById(commentVo.getParentId());
                if (parentCommentVo != null && parentCommentVo.getParentId() != null) {
                    commentVo.setParentId(parentCommentVo.getParentId());
                }
            }
            commentVo.setFcu(UserContext.get().getUserUuid(true));
            commentMapper.insertComment(commentVo);
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
