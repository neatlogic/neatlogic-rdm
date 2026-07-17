/*
 * Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved.
 */

package neatlogic.module.rdm.notify.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.rdm.notify.core.IRdmNotifyPolicyHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 按RDM业务分类定位通知策略处理器。
 */
@RootComponent
public class RdmNotifyPolicyHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IRdmNotifyPolicyHandler> HANDLER_MAP = new HashMap<>();

    public static IRdmNotifyPolicyHandler getHandler(String bizType) {
        return HANDLER_MAP.get(bizType);
    }

    @Override
    public void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IRdmNotifyPolicyHandler> beanMap = context.getBeansOfType(IRdmNotifyPolicyHandler.class);
        for (IRdmNotifyPolicyHandler handler : beanMap.values()) {
            if (!handler.isPublic()) {
                continue;
            }
            IRdmNotifyPolicyHandler oldHandler = HANDLER_MAP.put(handler.getBizType(), handler);
            if (oldHandler != null && oldHandler != handler) {
                throw new IllegalStateException("RDM通知分类" + handler.getBizType() + "存在重复处理器");
            }
        }
    }

    @Override
    protected void myInit() {
    }
}
