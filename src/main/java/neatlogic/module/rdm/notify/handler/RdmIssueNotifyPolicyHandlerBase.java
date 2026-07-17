/*
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 */

package neatlogic.module.rdm.notify.handler;

import neatlogic.framework.notify.dto.NotifyTriggerVo;
import neatlogic.framework.rdm.notify.constvalue.RdmIssueNotifyTriggerType;
import neatlogic.framework.rdm.notify.constvalue.RdmNotifyParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Issue型App通知处理器基类。
 */
public abstract class RdmIssueNotifyPolicyHandlerBase extends RdmNotifyPolicyHandlerBase {
    @Override
    protected boolean isIssueNotifyHandler() {
        return true;
    }

    @Override
    protected List<NotifyTriggerVo> myCustomNotifyTriggerList() {
        List<NotifyTriggerVo> resultList = new ArrayList<>();
        for (RdmIssueNotifyTriggerType triggerType : RdmIssueNotifyTriggerType.values()) {
            resultList.add(new NotifyTriggerVo(triggerType));
        }
        return resultList;
    }

    @Override
    protected List<RdmNotifyParam> myCustomSystemParamList() {
        return Arrays.asList(
                RdmNotifyParam.ISSUE_ID,
                RdmNotifyParam.ISSUE_NAME,
                RdmNotifyParam.APP_TYPE,
                RdmNotifyParam.APP_NAME,
                RdmNotifyParam.STATUS_NAME,
                RdmNotifyParam.OLD_STATUS_NAME,
                RdmNotifyParam.WORKER_LIST,
                RdmNotifyParam.OLD_WORKER_LIST,
                RdmNotifyParam.CREATOR,
                RdmNotifyParam.PRIORITY_NAME,
                RdmNotifyParam.ISSUE_CONTENT,
                RdmNotifyParam.COMMENT_CONTENT,
                RdmNotifyParam.ISSUE_URL
        );
    }
}
