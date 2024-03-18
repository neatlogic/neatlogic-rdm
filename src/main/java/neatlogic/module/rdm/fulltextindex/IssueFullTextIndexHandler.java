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
