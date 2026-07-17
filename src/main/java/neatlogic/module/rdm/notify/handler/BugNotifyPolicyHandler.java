/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.handler;

import neatlogic.framework.rdm.enums.AppType;
import org.springframework.stereotype.Component;

/**
 * 缺陷通知策略处理器。
 */
@Component
public class BugNotifyPolicyHandler extends RdmIssueNotifyPolicyHandlerBase {
    @Override
    public String getName() {
        return AppType.BUG.getLabel();
    }

    @Override
    public String getBizType() {
        return AppType.BUG.getName();
    }
}
