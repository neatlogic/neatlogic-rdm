/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectcategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectCategoryMapper;
import codedriver.module.rdm.dto.CategoryVo;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-24 14:14
 **/
@Service
public class ProjectCategoryChildSearchApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectCategoryMapper categoryMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectcategory/childSearch";
    }

    @Override
    public String getName() {
        return "项目子类别集合检索接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "uuid", type = ApiParamType.STRING, desc = "主键uuid", isRequired = true)})
    @Output({@Param(name = "categoryList", explode = CategoryVo[].class, desc = "子分类集合")})
    @Description(desc = "项目子类别集合检索接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setParentUuid(jsonObj.getString("uuid"));
        List<CategoryVo> categoryVoList = categoryMapper.searchProjectCategory(categoryVo);
        JSONObject returnObj = new JSONObject();
        returnObj.put("categoryList", categoryVoList);
        return returnObj;
    }
}
