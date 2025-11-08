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
