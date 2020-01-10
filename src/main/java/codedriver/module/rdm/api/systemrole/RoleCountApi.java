package codedriver.module.rdm.api.systemrole;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
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
 * @create: 2020-01-06 10:53
 **/
@Service
public class RoleCountApi extends ApiComponentBase {

    @Autowired
    private RoleService roleService;

    @Override
    public String getToken() {
        return "module/rdm/role/count";
    }

    @Override
    public String getName() {
        return "系统角色数量查询接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "userId", type = ApiParamType.STRING, desc = "用户id"),
            @Param(name = "role", type = ApiParamType.STRING, desc = "角色名称")})
    @Output({@Param(name = "count", type = ApiParamType.INTEGER, desc = "数量")})
    @Description(desc = "系统角色数量查询接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String userId = jsonObj.getString("userId");
        String role = jsonObj.getString("role");
        RoleVo roleVo = new RoleVo();
        roleVo.setRole(role);
        roleVo.setUserId(userId);
        int count = roleService.getRoleCount(roleVo);
        JSONObject returnObj = new JSONObject();
        returnObj.put("count", count);
        return returnObj;
    }
}
