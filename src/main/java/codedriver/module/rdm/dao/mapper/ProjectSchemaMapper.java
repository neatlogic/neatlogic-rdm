/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.dao.mapper;

import codedriver.framework.rdm.dto.ObjectAttrVo;
import org.apache.ibatis.annotations.Param;

public interface ProjectSchemaMapper {
    int checkTableIsExists(@Param("schemaName") String schemaName, @Param("tableName") String tableName);

    int checkTableHasData(String tableName);

    int checkSchemaIsExists(String databaseName);

    void insertObjectTableAttr(@Param("tableName") String tableName, @Param("objectAttrVo") ObjectAttrVo objectAttrVo);

    void insertObjectTable(@Param("tableName") String tableName);

    void deleteObjectTable(@Param("tableName") String tableName);

    void deleteObjectTableAttr(@Param("tableName") String tableName, @Param("objectAttrVo") ObjectAttrVo objectAttrVo);
}
