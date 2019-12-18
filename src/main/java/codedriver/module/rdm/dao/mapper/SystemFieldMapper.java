package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.FieldVo;

import java.util.List;

/**
 * @ClassName SystemFieldMapper
 * @Description
 * @Auther
 * @Date 2019/12/4 14:34
 **/
public interface SystemFieldMapper {
    List<FieldVo> searchField(FieldVo fieldVo);

    void insertSystemField(FieldVo fieldVo);
}
