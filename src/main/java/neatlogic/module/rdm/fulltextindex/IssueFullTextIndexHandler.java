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

package neatlogic.module.rdm.fulltextindex;

import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerBase;
import neatlogic.framework.fulltextindex.core.IFullTextIndexType;
import neatlogic.framework.fulltextindex.dto.fulltextindex.FullTextIndexTypeVo;
import neatlogic.framework.fulltextindex.dto.fulltextindex.FullTextIndexVo;
import neatlogic.framework.fulltextindex.dto.globalsearch.DocumentVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.IssueFullTextIndexType;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IssueFullTextIndexHandler extends FullTextIndexHandlerBase {
    @Resource
    private IssueMapper issueMapper;


    @Override
    protected String getModuleId() {
        return "rdm";
    }

    @Override
    protected void myCreateIndex(FullTextIndexVo fullTextIndexVo) {
        Long issueId = fullTextIndexVo.getTargetId();
        IssueVo issueVo = issueMapper.getIssueById(issueId);
        if (issueVo != null) {
            fullTextIndexVo.addFieldContent("name", new FullTextIndexVo.WordVo(issueVo.getName()));
            fullTextIndexVo.addFieldContent("content", new FullTextIndexVo.WordVo(issueVo.getContent()));
        }
    }

    @Override
    protected void myMakeupDocument(DocumentVo documentVo) {

    }

    @Override
    public IFullTextIndexType getType() {
        return IssueFullTextIndexType.ISSUE;
    }

    @Override
    public void myRebuildIndex(FullTextIndexTypeVo fullTextIndexTypeVo) {
        fullTextIndexTypeVo.setPageSize(100);
        List<Long> issueIdList = issueMapper.getNotIndexIssueIdList(fullTextIndexTypeVo);
        while (CollectionUtils.isNotEmpty(issueIdList)) {
            for (Long issueId : issueIdList) {
                this.createIndex(issueId, true);
            }
            issueIdList = issueMapper.getNotIndexIssueIdList(fullTextIndexTypeVo);
        }
    }
}
