package neatlogic.module.rdm.api.event;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.event.RdmEventRuntimeCapabilityFactory;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import org.springframework.stereotype.Service;

/** 普通 RDM 的只读能力发现入口，不暴露配置或商业授权信息。 */
@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetRdmEventCapabilityApi extends PrivateApiComponentBase {
    /** 返回接口名称。 */
    @Override
    public String getName() { return "查询研发事件能力"; }
    /** 本接口不需要组件配置。 */
    @Override
    public String getConfig() { return null; }
    /** 公共和商业环境均保留能力发现路径。 */
    @Override
    public String getToken() { return "/rdm/event/capability/get"; }
    /** 未安装或未授权时返回关闭状态，前端无需试探商业接口。 */
    @Override
    @Input({})
    @Output({@Param(name = "available", type = ApiParamType.BOOLEAN, desc = "事件配置与执行是否可用")})
    @Description(desc = "查询研发事件配置与执行能力")
    public Object myDoService(JSONObject param) {
        JSONObject result = new JSONObject();
        result.put("available", RdmEventRuntimeCapabilityFactory.isAvailable());
        return result;
    }
}
