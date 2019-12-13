package codedriver.module.rdm.api.project;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName ProjectDeleteApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectDeleteApi extends ApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/project/delete";
    }

    @Override
    public String getName() {
        return "查询项目接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "uuid", type = ApiParamType.STRING, isRequired = true)
    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        //删除项目

        //删除项目属性

        //删除项目文件

        //删除项目任务

        //删除项目过程域
        return null;
    }
}
