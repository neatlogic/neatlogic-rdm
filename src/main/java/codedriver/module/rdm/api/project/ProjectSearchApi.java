package codedriver.module.rdm.api.project;

import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName FieldSaveApi
 * @Description
 * @Auther fandong
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectSearchApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/project/search";
    }

    @Override
    public String getName() {
        return "查询项目接口";
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
