/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.module.rdm.service;

import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.exception.database.DataBaseNotFoundException;
import neatlogic.framework.rdm.dto.ObjectAttrVo;
import neatlogic.framework.rdm.dto.ObjectVo;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.dao.mapper.ProjectSchemaMapper;
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
