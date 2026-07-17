/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.api.notify;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.notify.dto.InvokeNotifyPolicyConfigVo;
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
 * 保存项目或App_type级通知策略覆盖配置。
 */
@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveRdmNotifyPolicyConfigApi extends PrivateApiComponentBase {

    @Resource
    private RdmNotifyConfigService rdmNotifyConfigService;

    @Override
    public String getName() { return "保存RDM通知策略配置"; }

    @Override
    public String getConfig() { return null; }

    @Override
    public String getToken() { return "/rdm/notify/policy/config/save"; }

    @Input({
            @Param(name = "targetType", type = ApiParamType.ENUM, rule = "project,app", isRequired = true, desc = "配置目标类型"),
            @Param(name = "targetId", type = ApiParamType.LONG, isRequired = true, desc = "配置目标id"),
            @Param(name = "policyId", type = ApiParamType.LONG, desc = "通知策略id"),
            @Param(name = "isCustom", type = ApiParamType.ENUM, rule = "0,1", isRequired = true, desc = "是否使用自定义策略"),
            @Param(name = "paramMappingList", type = ApiParamType.JSONARRAY, desc = "参数映射列表"),
            @Param(name = "excludeTriggerList", type = ApiParamType.JSONARRAY, desc = "停用触发点列表")
    })
    @Output({})
    @Description(desc = "保存RDM通知策略配置")
    @Override
    public Object myDoService(JSONObject paramObj) {
        InvokeNotifyPolicyConfigVo configVo = paramObj.toJavaObject(InvokeNotifyPolicyConfigVo.class);
        rdmNotifyConfigService.saveConfig(paramObj.getString("targetType"), paramObj.getLong("targetId"), configVo);
        return null;
    }
}
