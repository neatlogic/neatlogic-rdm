/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.message.handler;

import neatlogic.framework.message.core.MessageHandlerBase;
import neatlogic.framework.notify.dto.NotifyVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RDM站内消息处理器。
 */
@Service
public class RdmMessageHandler extends MessageHandlerBase {
    @Override
    public String getName() {
        return "研发管理通知";
    }

    @Override
    public String getDescription() {
        return "实时显示项目、迭代和事项相关通知";
    }

    @Override
    public boolean getNeedCompression() {
        return false;
    }

    @Override
    public NotifyVo compress(List<NotifyVo> notifyVoList) {
        return null;
    }
}
