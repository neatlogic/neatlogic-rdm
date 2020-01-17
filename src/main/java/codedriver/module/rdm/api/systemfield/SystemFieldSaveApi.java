package codedriver.module.rdm.api.systemfield;

import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.SystemFieldMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName FieldSaveApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class SystemFieldSaveApi extends ApiComponentBase {

    @Resource
    private SystemFieldMapper systemFieldMapper;

    @Override
    public String getToken() {
        return "module/rdm/systemfield/save";
    }

    @Override
    public String getName() {
        return "保存系统属性接口";
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
