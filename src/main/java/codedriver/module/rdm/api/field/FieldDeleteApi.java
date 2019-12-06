package codedriver.module.rdm.api.field;

import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.core.ApiRequestType;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName FieldDeleteApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
@ApiRequestType(type = "DELETE")
public class FieldDeleteApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/field/delete";
    }

    @Override
    public String getName() {
        return "删除系统属性接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        return null;
    }


}
