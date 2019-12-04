package codedriver.module.rdm.api.projectfield;

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
public class ProjectFieldDeleteApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/projectfield/delete";
    }

    @Override
    public String getName() {
        return "删除项目属性接口";
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
