/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.service;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.exception.database.DataBaseNotFoundException;
import codedriver.framework.rdm.dto.ObjectAttrVo;
import codedriver.framework.rdm.dto.ObjectVo;
import codedriver.framework.transaction.core.EscapeTransactionJob;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dao.mapper.ProjectSchemaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectSchemaMapper projectSchemaMapper;

    @Resource
    private ProjectMapper projectMapper;

    public EscapeTransactionJob.State dropObjectSchema(ObjectVo objectVo) {
        return new EscapeTransactionJob(() -> {
            if (projectSchemaMapper.checkSchemaIsExists(TenantContext.get().getDataDbName()) > 0) {
                projectSchemaMapper.deleteObjectTable(objectVo.getTableName());
            } else {
                throw new DataBaseNotFoundException();
            }
        }).execute();
    }


    public EscapeTransactionJob.State buildObjectSchema(ObjectVo objectVo) {
        return new EscapeTransactionJob(() -> {
            if (projectSchemaMapper.checkSchemaIsExists(TenantContext.get().getDataDbName()) > 0) {
                if (projectSchemaMapper.checkTableIsExists(TenantContext.get().getDataDbName(), "rdm_object_" + objectVo.getId()) <= 0) {
                    //创建配置项表
                    projectSchemaMapper.insertObjectTable(objectVo.getTableName());
                } else {
                    //如果已存在但没有数据，重建表
                    if (projectSchemaMapper.checkTableHasData(objectVo.getTableName()) <= 0) {
                        projectSchemaMapper.deleteObjectTable(objectVo.getTableName());
                        projectSchemaMapper.insertObjectTable(objectVo.getTableName());
                        for (ObjectAttrVo attrVo : objectVo.getAttrList()) {
                            if (attrVo.getIsPrivate().equals(0)) {
                                projectSchemaMapper.insertObjectTableAttr(objectVo.getTableName(), attrVo);
                            }
                        }
                    }
                }
            } else {
                throw new DataBaseNotFoundException();
            }
        }).execute();
    }
}
