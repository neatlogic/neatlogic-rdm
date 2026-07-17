/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.notify.service;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.notify.dto.InvokeNotifyPolicyConfigVo;

/**
 * RDM项目和应用通知策略覆盖配置服务。
 */
public interface RdmNotifyConfigService {

    JSONObject getConfig(String targetType, Long targetId);

    void saveConfig(String targetType, Long targetId, InvokeNotifyPolicyConfigVo configVo);
}
