package codedriver.module.rdm.api.systemrole;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.services.RoleService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 11:07
 **/
@Service
public class RoleDeleteApi extends ApiComponentBase {

    @Autowired
    private RoleService roleService;

    @Override
    public String getToken() {
        return "module/rdm/role/delete";
    }

    @Override
    public String getName() {
        return "系统角色删除接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "userIdList", type = ApiParamType.JSONARRAY, isRequired = true, desc = "用户ID集合")})
    @Description( desc = "系统角色删除接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONArray array = jsonObj.getJSONArray("userIdList");
        for (int i = 0; i < array.size(); i++){
            roleService.deleteRoleByUserId(array.getString(i));
        }
        return new JSONObject();
    }
}
