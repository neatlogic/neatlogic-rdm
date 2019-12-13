package codedriver.module.rdm.api.taskfilter;

import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.SystemFieldMapper;
import codedriver.module.rdm.dto.FieldVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FilterSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class TaskFilterSearchApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/taskfilter/search";
    }

    @Override
    public String getName() {
        return "查询过滤器接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject data = new JSONObject();
        return data;
    }

}
