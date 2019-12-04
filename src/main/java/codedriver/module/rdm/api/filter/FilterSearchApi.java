package codedriver.module.rdm.api.filter;

import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.FieldMapper;
import codedriver.module.rdm.dto.FieldVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FieldSaveApi
 * @Description
 * @Auther fandong
 * @Date 2019/12/4 9:52
 **/
@Service
public class FilterSearchApi extends ApiComponentBase {

    @Resource
    private FieldMapper fieldMapper;

    @Override
    public String getToken() {
        return "module/rdm/filter/search";
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
        List<FieldVo> fieldList =  fieldMapper.getFieldList(new FieldVo());
        data.put("fieldList", fieldList);
        return data;
    }

}
