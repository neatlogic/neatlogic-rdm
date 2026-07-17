/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.handler;

import neatlogic.framework.rdm.enums.AppType;
import org.springframework.stereotype.Component;

/**
 * 需求通知策略处理器。
 */
@Component
public class StoryNotifyPolicyHandler extends RdmIssueNotifyPolicyHandlerBase {
    @Override
    public String getName() {
        return AppType.STORY.getLabel();
    }

    @Override
    public String getBizType() {
        return AppType.STORY.getName();
    }
}
