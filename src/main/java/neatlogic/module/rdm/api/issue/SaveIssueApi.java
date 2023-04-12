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
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerFactory;
import neatlogic.framework.fulltextindex.core.IFullTextIndexHandler;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.IssueAttrVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.TagVo;
import neatlogic.framework.rdm.enums.IssueFullTextIndexType;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.dao.mapper.TagMapper;
import org.apache.commons.collections4.CollectionUtils;
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
    private AppMapper appMapper;
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private TagMapper tagMapper;


    @Override
    public String getName() {
        return "保存任务";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "id，不提供代表新增任务"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "应用id", isRequired = true),
            @Param(name = "name", type = ApiParamType.STRING, xss = true, isRequired = true, maxLength = 50, desc = "任务名称"),
            @Param(name = "priority", type = ApiParamType.LONG, desc = "优先级"),
            @Param(name = "catalog", type = ApiParamType.LONG, desc = "目录"),
            @Param(name = "tagList", type = ApiParamType.JSONARRAY, desc = "标签"),
            @Param(name = "status", type = ApiParamType.LONG, desc = "状态"),
            @Param(name = "attrList", type = ApiParamType.JSONARRAY, desc = "自定义属性列表"),
            @Param(name = "userIdList", type = ApiParamType.JSONARRAY, desc = "用户列表")})
    @Output({@Param(name = "id", type = ApiParamType.LONG, desc = "任务id")})
    @Description(desc = "保存任务接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IssueVo issueVo = JSONObject.toJavaObject(paramObj, IssueVo.class);
        Long id = paramObj.getLong("id");
        List<AppAttrVo> appAttrList = appMapper.getAttrByAppId(issueVo.getAppId());
        //补充页面没有提供的自定义属性
        for (AppAttrVo appAttrVo : appAttrList) {
            if (appAttrVo.getIsPrivate().equals(0)) {
                if (issueVo.getAttr(appAttrVo.getId()) == null) {
                    issueVo.addAttr(new IssueAttrVo(appAttrVo.getId(), issueVo.getId(), appAttrVo.getType()));
                } else {
                    issueVo.getAttr(appAttrVo.getId()).setAttrType(appAttrVo.getType());
                }
            }
        }
        if (id == null) {
            issueMapper.insertIssue(issueVo);
            if (CollectionUtils.isNotEmpty(issueVo.getAttrList())) {
                issueMapper.insertIssueAttr(issueVo);
            }
        } else {
            issueMapper.updateIssue(issueVo);
            if (CollectionUtils.isNotEmpty(issueVo.getAttrList())) {
                issueMapper.updateIssueAttr(issueVo);
            }
            issueMapper.deleteIssueTagByIssueId(issueVo.getId());
            issueMapper.deleteIssueUserByIssueId(issueVo.getId());
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
