/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

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
                if (projectSchemaMapper.checkTableIsExists(TenantContext.get().getDataDbName(), "rdm_app_" + appVo.getId()) <= 0) {
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
