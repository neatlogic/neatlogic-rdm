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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerFactory;
import neatlogic.framework.fulltextindex.core.IFullTextIndexHandler;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.enums.IssueFullTextIndexType;
import neatlogic.framework.rdm.enums.IssueRelType;
import neatlogic.framework.rdm.exception.ProjectNotAuthException;
import neatlogic.framework.rdm.exception.ProjectNotFoundException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveIssueApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private AttrMapper attrMapper;
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private TagMapper tagMapper;


    @Override
    public String getName() {
        return "nmrai.saveissueapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "id", type = ApiParamType.LONG, desc = "nmrai.saveissueapi.input.param.desc.id"),
            @Param(name = "fromId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.fromid"),
            @Param(name = "toId", type = ApiParamType.LONG, desc = "nmrai.searchissueapi.input.param.desc.toid"),
            @Param(name = "relType", type = ApiParamType.ENUM, member = IssueRelType.class, desc = "common.reltype"),
            @Param(name = "parentId", type = ApiParamType.LONG, desc = "term.rdm.parenttaskid"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc", isRequired = true),
            @Param(name = "name", type = ApiParamType.STRING, xss = true, isRequired = true, maxLength = 50, desc = "nmrai.saveissueapi.input.param.desc.name"),
            @Param(name = "priority", type = ApiParamType.LONG, desc = "common.priority"),
            @Param(name = "iteration", type = ApiParamType.LONG, desc = "common.iteration"),
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "common.catalog"),
            @Param(name = "tagList", type = ApiParamType.JSONARRAY, desc = "common.tag"),
            @Param(name = "status", type = ApiParamType.LONG, desc = "common.status"),
            @Param(name = "attrList", type = ApiParamType.JSONARRAY, desc = "nmrai.saveissueapi.input.param.desc.attrlist"),
            @Param(name = "userIdList", type = ApiParamType.JSONARRAY, desc = "common.userlist"),
            @Param(name = "comment", type = ApiParamType.STRING, desc = "common.comment")})
    @Output({@Param(name = "id", type = ApiParamType.LONG, desc = "term.rdm.issueid")})
    @Description(desc = "nmrai.saveissueapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long fromId = paramObj.getLong("fromId");
        Long toId = paramObj.getLong("toId");
        String relType = paramObj.getString("relType");
        Long appId = paramObj.getLong("appId");
        ProjectVo projectVo = projectMapper.getProjectByAppId(appId);
        if (projectVo == null) {
            throw new ProjectNotFoundException();
        }
        if (!projectVo.getIsOwner() && !projectVo.getIsLeader() && !projectVo.getIsMember()) {
            throw new ProjectNotAuthException(projectVo.getName());
        }
        IssueVo issueVo = JSONObject.toJavaObject(paramObj, IssueVo.class);
        issueVo.setCreateUser(UserContext.get().getUserUuid(true));
        issueVo.formatAttr();
        Long id = paramObj.getLong("id");
        List<AppAttrVo> appAttrList = attrMapper.getAttrByAppId(issueVo.getAppId());
        //补充页面没有提供的自定义属性
        for (AppAttrVo appAttrVo : appAttrList) {
            if (appAttrVo.getIsPrivate().equals(0)) {
                if (issueVo.getAttr(appAttrVo.getId()) == null) {
                    issueVo.addAttr(new IssueAttrVo(appAttrVo.getId(), issueVo.getId(), appAttrVo.getType(), appAttrVo.getConfig()));
                } else {
                    issueVo.getAttr(appAttrVo.getId()).setAttrType(appAttrVo.getType()).setConfig(appAttrVo.getConfig());
                }
            }
        }
        if (id == null) {
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
                issueMapper.insertIssueUser(issueVo.getId(), userId.replace(GroupSearch.USER.getValuePlugin(), ""));
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
        if (fromId != null) {
            IssueVo fromIssue = issueMapper.getIssueById(fromId);
            if (fromIssue != null) {
                IssueRelVo issueRelVo = new IssueRelVo(fromIssue.getAppId(), fromIssue.getId(), issueVo.getAppId(), issueVo.getId(), StringUtils.isNotBlank(relType) ? relType : IssueRelType.EXTEND.getValue());
                issueMapper.insertIssueRel(issueRelVo);
            }
        } else if (toId != null) {
            IssueVo toIssue = issueMapper.getIssueById(toId);
            if (toIssue != null) {
                IssueRelVo issueRelVo = new IssueRelVo(issueVo.getAppId(), issueVo.getId(), toIssue.getAppId(), toIssue.getId(), StringUtils.isNotBlank(relType) ? relType : IssueRelType.EXTEND.getValue());
                issueMapper.insertIssueRel(issueRelVo);
            }
        }
        //创建全文检索索引
        IFullTextIndexHandler indexHandler = FullTextIndexHandlerFactory.getHandler(IssueFullTextIndexType.ISSUE);
        if (indexHandler != null) {
            indexHandler.createIndex(issueVo.getId());
        }
        return issueVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/issue/save";
    }

}
