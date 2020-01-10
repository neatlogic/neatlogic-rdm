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

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-09 14:24
 **/
@Service
public class RoleSearchApi extends ApiComponentBase {

    @Autowired
    private RoleService roleService;

    @Override
    public String getToken() {
        return "/module/rdm/role/search";
    }

    @Override
    public String getName() {
        return "系统角色查询接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "role", type = ApiParamType.STRING, desc = "角色类型", isRequired = true)})
    @Output({@Param(name = "roleUserList", explode = RoleVo[].class, desc = "角色用户集合")})
    @Description(desc = "系统角色查询接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String role = jsonObj.getString("role");
        RoleVo roleVo = new RoleVo();
        roleVo.setRole(role);
        List<RoleVo> roleUserList = roleService.searchRole(roleVo);
        JSONObject returnObj = new JSONObject();
        returnObj.put("roleUserList", roleUserList);
        return returnObj;
    }
}
