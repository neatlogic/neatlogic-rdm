package codedriver.module.rdm.api.projectcategory;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.services.ProjectCategoryService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-24 14:48
 **/
@Service
public class ProjectCategoryDeleteApi extends ApiComponentBase {

    @Autowired
    private ProjectCategoryService categoryService;

    @Override
    public String getToken() {
        return "module/rdm/projectcategory/delete";
    }

    @Override
    public String getName() {
        return "项目类别移除接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "uuid", type = ApiParamType.STRING, desc = "类别uuid", isRequired = true)})
    @Description(desc = "项目类别移除接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String uuid = jsonObj.getString("uuid");
        categoryService.deleteProjectCategory(uuid);
        return new JSONObject();
    }
}
