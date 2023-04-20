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
