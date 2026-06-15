/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.module.rdm.issuestat;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IssueStatHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IIssueStatHandler> componentMap = new HashMap<>();
    @Resource
    private List<IIssueStatHandler> issueStatHandlerList;

    public static IIssueStatHandler getHandler(String statKey) {
        return componentMap.get(statKey);
    }

    @PostConstruct
    public void initHandler() {
        register(issueStatHandlerList);
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IIssueStatHandler> map = context.getBeansOfType(IIssueStatHandler.class);
        register(map.values());
    }

    private void register(Iterable<IIssueStatHandler> handlerList) {
        if (handlerList == null) {
            return;
        }
        for (IIssueStatHandler handler : handlerList) {
            componentMap.put(handler.getStatKey(), handler);
        }
    }

    @Override
    protected void myInit() {

    }
}
