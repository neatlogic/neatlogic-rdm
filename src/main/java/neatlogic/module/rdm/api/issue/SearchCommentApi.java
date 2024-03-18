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

package neatlogic.module.rdm.api.issue;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.CommentVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchCommentApi extends PrivateApiComponentBase {


    @Resource
    private CommentMapper commentMapper;

    @Override
    public String getName() {
        return "搜索评论";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "issueId", type = ApiParamType.LONG, isRequired = true, desc = "任务id"),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "父评论id"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "当前页"),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "每页大小")})
    @Output({@Param(explode = BasePageVo.class), @Param(name = "tbodyList", explode = CommentVo[].class)})
    @Description(desc = "搜索评论接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        CommentVo commentVo = JSONObject.toJavaObject(paramObj, CommentVo.class);
        int rowNum = commentMapper.searchCommentCount(commentVo);
        commentVo.setRowNum(rowNum);
        List<CommentVo> commentList = null;
        if (rowNum > 0) {
            commentList = commentMapper.searchComment(commentVo);
        }
        return TableResultUtil.getResult(commentList, commentVo);
    }

    @Override
    public String getToken() {
        return "/rdm/issue/comment/search";
    }
}
