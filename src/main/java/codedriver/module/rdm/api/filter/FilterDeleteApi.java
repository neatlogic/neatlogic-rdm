package codedriver.module.rdm.api.filter;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName FilterDeleteApi
 * @Description
 * @Auther r2d2
 * @Date 2019/12/4 9:52
 **/
@Service
public class FilterDeleteApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/filter/delete";
    }

    @Override
    public String getName() {
        return "删除过滤器接口";
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
