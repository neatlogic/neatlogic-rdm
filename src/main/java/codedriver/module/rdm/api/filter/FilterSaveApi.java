package codedriver.module.rdm.api.filter;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName FilterSaveApi
 * @Description
 * @Auther r2d2
 * @Date 2019/12/4 9:52
 **/
@Service
public class FilterSaveApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/filter/save";
    }

    @Override
    public String getName() {
        return "保存过滤器接口";
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
