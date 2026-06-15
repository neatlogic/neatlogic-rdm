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

package neatlogic.module.rdm.issuestat.handler;

import neatlogic.framework.rdm.dto.IssueStatContextVo;
import neatlogic.framework.rdm.dto.IssueStatResultVo;
import neatlogic.framework.rdm.enums.AppStatAttrType;
import neatlogic.module.rdm.issuestat.IIssueStatHandler;
import org.springframework.stereotype.Component;

@Component
public class StoryPriorityDistributionStatHandler extends IssueStatHandlerBase implements IIssueStatHandler {
    @Override
    public String getStatKey() {
        return AppStatAttrType.STORY_PRIORITY_DISTRIBUTION.getValue();
    }

    @Override
    public IssueStatResultVo calculate(IssueStatContextVo context) {
        IssueStatResultVo resultVo = createResult(context, "column", "优先级分布");
        resultVo.setFilterField("priority");
        resultVo.setDataList(toJsonArray(issueMapper.getIssueOverviewPriorityList(toIssueVo(context))));
        return resultVo;
    }
}
