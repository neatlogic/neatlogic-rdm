/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.handler;

import neatlogic.framework.notify.dto.NotifyTriggerVo;
import neatlogic.framework.rdm.enums.AppType;
import neatlogic.framework.rdm.notify.constvalue.RdmIterationNotifyTriggerType;
import neatlogic.framework.rdm.notify.constvalue.RdmNotifyParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 迭代通知策略处理器。
 */
@Component
public class RdmIterationNotifyPolicyHandler extends RdmNotifyPolicyHandlerBase {
    @Override
    public String getName() {
        return AppType.ITERATION.getLabel();
    }

    @Override
    public String getBizType() {
        return AppType.ITERATION.getName();
    }

    @Override
    protected List<NotifyTriggerVo> myCustomNotifyTriggerList() {
        List<NotifyTriggerVo> resultList = new ArrayList<>();
        for (RdmIterationNotifyTriggerType triggerType : RdmIterationNotifyTriggerType.values()) {
            resultList.add(new NotifyTriggerVo(triggerType));
        }
        return resultList;
    }

    @Override
    protected List<RdmNotifyParam> myCustomSystemParamList() {
        return Arrays.asList(
                RdmNotifyParam.ITERATION_ID,
                RdmNotifyParam.ITERATION_NAME,
                RdmNotifyParam.ITERATION_DESCRIPTION,
                RdmNotifyParam.ITERATION_START_DATE,
                RdmNotifyParam.ITERATION_END_DATE,
                RdmNotifyParam.ITERATION_IS_OPEN,
                RdmNotifyParam.ITERATION_ISSUE_COUNT,
                RdmNotifyParam.ITERATION_DONE_ISSUE_COUNT,
                RdmNotifyParam.ITERATION_COMPLETE_RATE,
                RdmNotifyParam.ITERATION_URL
        );
    }
}
