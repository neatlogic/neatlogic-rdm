/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.module.rdm.service;

import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.exception.database.DataBaseNotFoundException;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.ProjectSchemaMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectSchemaMapper projectSchemaMapper;


    public EscapeTransactionJob.State dropObjectSchema(AppVo objectVo) {
        return new EscapeTransactionJob(() -> {
            if (projectSchemaMapper.checkSchemaIsExists(TenantContext.get().getDataDbName()) > 0) {
                projectSchemaMapper.deleteAppTable(objectVo.getTableName());
            } else {
                throw new DataBaseNotFoundException();
            }
        }).execute();
    }


    public EscapeTransactionJob.State buildObjectSchema(AppVo appVo) {
        return new EscapeTransactionJob(() -> {
            if (projectSchemaMapper.checkSchemaIsExists(TenantContext.get().getDataDbName()) > 0) {
                if (projectSchemaMapper.checkTableIsExists(TenantContext.get().getDataDbName(), "rdm_object_" + appVo.getId()) <= 0) {
                    //创建配置项表
                    projectSchemaMapper.insertAppTable(appVo.getTableName());
                    if (CollectionUtils.isNotEmpty(appVo.getAttrList())) {
                        for (AppAttrVo attrVo : appVo.getAttrList()) {
                            if (attrVo.getIsPrivate().equals(0)) {
                                projectSchemaMapper.insertAppTableAttr(appVo.getTableName(), attrVo);
                            }
                        }
                    }
                } else {
                    //如果已存在但没有数据，重建表
                    if (projectSchemaMapper.checkTableHasData(appVo.getTableName()) <= 0) {
                        projectSchemaMapper.deleteAppTable(appVo.getTableName());
                        projectSchemaMapper.insertAppTable(appVo.getTableName());
                        if (CollectionUtils.isNotEmpty(appVo.getAttrList())) {
                            for (AppAttrVo attrVo : appVo.getAttrList()) {
                                if (attrVo.getIsPrivate().equals(0)) {
                                    projectSchemaMapper.insertAppTableAttr(appVo.getTableName(), attrVo);
                                }
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
