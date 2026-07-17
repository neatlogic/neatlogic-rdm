/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.service;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.rdm.notify.dto.RdmNotifyContextVo;

/**
 * RDM通知运行时入口。
 */
public interface RdmNotifyService {

    /**
     * 在当前事务提交后异步发送通知。
     */
    void notify(RdmNotifyContextVo contextVo, INotifyTriggerType triggerType);
}
