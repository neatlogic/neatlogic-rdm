package codedriver.module.rdm.api.processarea;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProcessAreaMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ProcessAreaSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProcessAreaGetApi extends ApiComponentBase {

    @Resource
    private ProcessAreaMapper processAreaMapper;

    @Override
    public String getToken() {
        return "module/rdm/processarea/get";
    }

    @Override
    public String getName() {
        return "根据uuid查询过程域接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "uuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true)})
    @Description(desc = "根据uuid查询过程域接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String uuid = jsonObj.getString("uuid");
        result.put("processArea", processAreaMapper.getProcessAreaByUuid(uuid));
        result.put("fieldList", processAreaMapper.getProcessAreaFieldListByUuid(uuid));
        return result;
    }


}
