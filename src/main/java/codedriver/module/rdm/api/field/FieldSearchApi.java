package codedriver.module.rdm.api.field;

import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.FieldMapper;
import codedriver.module.rdm.dto.FieldVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FieldSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class FieldSearchApi extends ApiComponentBase {

    @Resource
    private FieldMapper fieldMapper;

    @Override
    public String getToken() {
        return "module/rdm/field/search";
    }

    @Override
    public String getName() {
        return "查询系统属性接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject data = new JSONObject();
        List<FieldVo> fieldList = fieldMapper.getFieldList(new FieldVo());
        data.put("fieldList", fieldList);
        return data;
    }

}
