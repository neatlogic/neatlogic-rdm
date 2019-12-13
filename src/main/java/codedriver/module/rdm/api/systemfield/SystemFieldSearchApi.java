package codedriver.module.rdm.api.systemfield;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.SystemFieldMapper;
import codedriver.module.rdm.dto.FieldVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FieldSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class SystemFieldSearchApi extends ApiComponentBase {

    @Resource
    private SystemFieldMapper systemFieldMapper;

    @Override
    public String getToken() {
        return "module/rdm/systemfield/search";
    }

    @Override
    public String getName() {
        return "查询系统属性接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "keyword", type = ApiParamType.STRING, desc = "关键字(属性名称),模糊查询", isRequired = false)

    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject data = new JSONObject();
        FieldVo fieldVo = new FieldVo();
        if(jsonObj.containsKey("keyword")){
            String keyword = jsonObj.getString("keyword");
            fieldVo.setKeyword(keyword);
        }

        //test
        List<FieldVo> fieldVoList = new ArrayList<FieldVo>(){{
            add(new FieldVo("name", "名称", "77941ed7405d4ecf9617f4c59ed5eef2", 5, "", 1));
            add(new FieldVo("statusName", "状态", "15bf75fee7c3415c88db5efb31b66755", 4, "", 2));
            add(new FieldVo("priorityName", "优先级", "d06b08e3420a47a79470dc140eb9736f", 4, "", 3));
            add(new FieldVo("createUser", "创建人", "b17475f97ee9472fa57f817f90a55bd3", 5, "", 4));
            add(new FieldVo("createTime", "创建时间", "de50b4b946c34f5ab0c9844ca503d72d", 3, "", 5));
            add(new FieldVo("processUser", "处理人", "70a1ee7ba73d4ff496d06c5099a60d27", 7, "", 6));
            add(new FieldVo("startTime", "开始时间", "bdfe8acb69734b0bb7a8781ca59886e9", 3, "", 7));
            add(new FieldVo("endTime", "截至时间", "10c62e2569234199a95fac6713cccbe3", 3, "", 8));
            add(new FieldVo("iterationName", "迭代", "cbc54be59a494e508ab181191a5f3ba4", 4, "", 9));
            add(new FieldVo("categoryName", "分类", "1cc7765ad13745bc90fc3d2d96abc0db", 4, "", 10));
            add(new FieldVo("updateUser", "更新人", "d4afcda53aef477db79116181f961c9f", 5, "", 11));
            add(new FieldVo("updateTime", "更新时间", "893798cdf7c64caea90595599b7fee28", 3, "", 12));
        }};


//        data.put("systemFieldList", systemFieldMapper.searchField(fieldVo));
        data.put("systemFieldList", fieldVoList);
        return data;
    }

}
