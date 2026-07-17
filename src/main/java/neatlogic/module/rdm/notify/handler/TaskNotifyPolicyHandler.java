/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.handler;

import neatlogic.framework.rdm.enums.AppType;
import org.springframework.stereotype.Component;

/**
 * 任务通知策略处理器。
 */
@Component
public class TaskNotifyPolicyHandler extends RdmIssueNotifyPolicyHandlerBase {
    @Override
    public String getName() {
        return AppType.TASK.getLabel();
    }

    @Override
    public String getBizType() {
        return AppType.TASK.getName();
    }
}
