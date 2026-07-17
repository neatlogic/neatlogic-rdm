/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.handler;

import neatlogic.framework.notify.dto.NotifyTriggerVo;
import neatlogic.framework.rdm.notify.constvalue.RdmProjectNotifyTriggerType;
import neatlogic.framework.rdm.notify.constvalue.RdmNotifyParam;
import neatlogic.framework.util.$;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * 项目通知策略处理器。
 */
@Component
public class RdmProjectNotifyPolicyHandler extends RdmNotifyPolicyHandlerBase {
    @Override
    public String getName() {
        return $.t("common.project");
    }

    @Override
    public String getBizType() {
        return "project";
    }

    @Override
    protected List<NotifyTriggerVo> myCustomNotifyTriggerList() {
        List<NotifyTriggerVo> resultList = new ArrayList<>();
        for (RdmProjectNotifyTriggerType triggerType : RdmProjectNotifyTriggerType.values()) {
            resultList.add(new NotifyTriggerVo(triggerType));
        }
        return resultList;
    }

    @Override
    protected List<RdmNotifyParam> myCustomSystemParamList() {
        return Arrays.asList(
                RdmNotifyParam.PROJECT_CONFIG_URL,
                RdmNotifyParam.OLD_PROJECT_NAME,
                RdmNotifyParam.PROJECT_DESCRIPTION,
                RdmNotifyParam.OLD_PROJECT_DESCRIPTION,
                RdmNotifyParam.OLD_PROJECT_START_DATE,
                RdmNotifyParam.OLD_PROJECT_END_DATE,
                RdmNotifyParam.PROJECT_MEMBER_SUMMARY,
                RdmNotifyParam.OLD_PROJECT_MEMBER_SUMMARY,
                RdmNotifyParam.PROJECT_IS_CLOSE
        );
    }
}
