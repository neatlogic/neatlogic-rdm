/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.api.notify;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Output;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.notify.service.RdmNotifyConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 获取项目或App_type级通知策略覆盖配置。
 */
@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetRdmNotifyPolicyConfigApi extends PrivateApiComponentBase {

    @Resource
    private RdmNotifyConfigService rdmNotifyConfigService;

    @Override
    public String getName() { return "获取RDM通知策略配置"; }

    @Override
    public String getConfig() { return null; }

    @Override
    public String getToken() { return "/rdm/notify/policy/config/get"; }

    @Input({
            @Param(name = "targetType", type = ApiParamType.ENUM, rule = "project,app", isRequired = true, desc = "配置目标类型"),
            @Param(name = "targetId", type = ApiParamType.LONG, isRequired = true, desc = "配置目标id")
    })
    @Output({@Param(name = "handler", type = ApiParamType.STRING, desc = "服务端生成的通知处理器")})
    @Description(desc = "获取RDM通知策略配置")
    @Override
    public Object myDoService(JSONObject paramObj) {
        return rdmNotifyConfigService.getConfig(paramObj.getString("targetType"), paramObj.getLong("targetId"));
    }
}
