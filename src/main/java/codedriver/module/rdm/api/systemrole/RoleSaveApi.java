package codedriver.module.rdm.api.systemrole;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.RoleVo;
import codedriver.module.rdm.services.RoleService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 10:33
 **/
@Service
public class RoleSaveApi extends ApiComponentBase {

    @Autowired
    private RoleService roleService;

    @Override
    public String getToken() {
        return "module/rdm/role/save";
    }

    @Override
    public String getName() {
        return "系统角色保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "userId", desc = "用户ID", isRequired = true, type = ApiParamType.STRING),
            @Param(name = "role", desc = "角色", isRequired = true, type = ApiParamType.STRING)})
    @Description(desc = "系统角色保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String userId = jsonObj.getString("userId");
        String role = jsonObj.getString("role");
        RoleVo roleVo = new RoleVo();
        roleVo.setRole(role);
        roleVo.setUserId(userId);
        roleService.saveRole(roleVo);
        return new JSONObject();
    }
}
