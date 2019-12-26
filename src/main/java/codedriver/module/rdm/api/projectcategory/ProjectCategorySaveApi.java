package codedriver.module.rdm.api.projectcategory;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectCategoryMapper;
import codedriver.module.rdm.dao.mapper.ProjectMemberMapper;
import codedriver.module.rdm.dto.CategoryVo;
import codedriver.module.rdm.util.UuidUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-24 11:15
 **/
@Service
public class ProjectCategorySaveApi extends ApiComponentBase {


    @Autowired
    private ProjectCategoryMapper categoryMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectcategory/save";
    }

    @Override
    public String getName() {
        return "项目类别保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
             @Param( name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
             @Param( name = "uuid", type = ApiParamType.STRING, desc = "主键uuid"),
             @Param( name = "parentUuid", type = ApiParamType.STRING, desc = "父级类别uuid"),
             @Param( name = "name", type = ApiParamType.STRING, desc = "类别名称", isRequired = true, xss = true),
             @Param( name = "description", type = ApiParamType.STRING, desc = "类别描述", xss = true)})
    @Output({ @Param( name = "uuid", type = ApiParamType.STRING, desc = "类别主键uuid")})
    @Description(desc = "项目类别保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        CategoryVo category = new CategoryVo();
        category.setProjectUuid(jsonObj.getString("projectUuid"));
        category.setProcessAreaUuid(jsonObj.getString("processAreaUuid"));
        category.setName(jsonObj.getString("name"));
        if (jsonObj.containsKey("uuid")){
            category.setUuid(jsonObj.getString("uuid"));
        }
        if (jsonObj.containsKey("parentUuid")){
            category.setParentUuid(jsonObj.getString("parentUuid"));
        }
        if (jsonObj.containsKey("description")){
            category.setDescription(jsonObj.getString("description"));
        }

        if (StringUtils.isNotBlank(category.getUuid())){
            categoryMapper.updateProjectCategoryByUuid(category);
        }else {
            category.setUuid(UuidUtil.getUuid());
            category.setIsActive(1);
            categoryMapper.insertProjectCategory(category);
        }
        JSONObject returnObj = new JSONObject();
        returnObj.put("uuid", category.getUuid());
        return returnObj;
    }
}
