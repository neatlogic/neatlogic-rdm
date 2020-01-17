package codedriver.module.rdm.api.processarea;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProcessAreaMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProcessAreaDeleteApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProcessAreaDeleteApi extends ApiComponentBase {

    @Resource
    private ProcessAreaMapper processAreaMapper;

    @Override
    public String getToken() {
        return "module/rdm/processarea/delete";
    }

    @Override
    public String getName() {
        return "删除过程域接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true)})
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        processAreaMapper.deleteProcessArea(jsonObj.getString("uuid"));
        return null;
    }


}
