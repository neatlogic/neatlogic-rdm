package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.CategoryVo;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-24 11:51
 **/
public interface ProjectCategoryMapper {

    void updateProjectCategoryByUuid(CategoryVo categoryVo);

    void insertProjectCategory(CategoryVo categoryVo);

    List<CategoryVo> searchProjectCategory(CategoryVo categoryVo);

    void deleteProjectCategoryByUuid(String uuid);
}
