package codedriver.module.rdm.services;

import codedriver.module.rdm.dao.mapper.ProjectCategoryMapper;
import codedriver.module.rdm.dto.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-24 14:54
 **/
@Transactional
@Service
public class ProjectCategoryServiceImpl implements ProjectCategoryService {

    @Autowired
    private ProjectCategoryMapper categoryMapper;

    @Override
    public void deleteProjectCategory(String uuid) {
        List<String> uuidList = new ArrayList<>();
        iterateChildCategory(uuid, uuidList);
        uuidList.add(uuid);
        for (String delUuid: uuidList){
            categoryMapper.deleteProjectCategoryByUuid(delUuid);
        }
    }

    public void iterateChildCategory(String uuid, List<String> uuidList){
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setParentUuid(uuid);
        List<CategoryVo> childCategoryList = categoryMapper.searchProjectCategory(categoryVo);
        if (childCategoryList.size() > 0){
            for (CategoryVo category : childCategoryList){
                uuidList.add(category.getUuid());
                iterateChildCategory(category.getUuid(), uuidList);
            }
        }
    }
}
