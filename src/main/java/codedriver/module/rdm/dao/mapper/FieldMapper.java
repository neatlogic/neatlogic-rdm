package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.FieldVo;

import java.util.List;

/**
 * @ClassName FieldMapper
 * @Description
 * @Auther r2d2
 * @Date 2019/12/4 14:34
 **/
public interface FieldMapper {
    List<FieldVo> getFieldList(FieldVo fieldVo);
}
