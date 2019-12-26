package codedriver.module.rdm.api.projectcategory;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectCategoryMapper;
import codedriver.module.rdm.dto.CategoryVo;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-24 14:08
 **/
@Service
public class ProjectCategorySearchApi extends ApiComponentBase {

    @Autowired
    private ProjectCategoryMapper categoryMapper;

    @Override
    public String getToken() {
        return "/module/rdm/projectcategory/search";
    }

    @Override
    public String getName() {
        return "项目类别首层/单个检索接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({ @Param( name = "uuid", type = ApiParamType.STRING, desc = "项目类别uuid"),
             @Param( name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
             @Param( name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true)})
    @Output({ @Param(name = "categoryList", explode = CategoryVo[].class, desc = "项目类别集合")})
    @Description(desc = "项目类别首层/单个检索接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        CategoryVo categoryVo = new CategoryVo();
        if (jsonObj.containsKey("uuid")){
            categoryVo.setUuid(jsonObj.getString("uuid"));
        }
        categoryVo.setProjectUuid(jsonObj.getString("projectUuid"));
        categoryVo.setProcessAreaUuid(jsonObj.getString("processAreaUuid"));
        List<CategoryVo> categoryVoList = categoryMapper.searchProjectCategory(categoryVo);
        if (categoryVoList.size() > 1){
            List<CategoryVo> rootCateGory = new ArrayList<>();
            for (CategoryVo category : categoryVoList){
                if (StringUtils.isBlank(category.getParentUuid())){
                    rootCateGory.add(category);
                }
            }
            categoryVoList = rootCateGory;
        }
        JSONObject returnObj = new JSONObject();
        returnObj.put("categoryList", categoryVoList);
        return returnObj;
    }
}
