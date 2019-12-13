package codedriver.module.rdm.api.taskfilter;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName FilterSaveApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class TaskFilterSaveApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/taskfilter/save";
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
