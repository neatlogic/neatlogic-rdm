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

package neatlogic.module.rdm.startup.handler;

import neatlogic.framework.rdm.attrhandler.code.AttrHandlerFactory;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.startup.StartupBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplyPrivateAttrStartupHandler extends StartupBase {
    @Resource
    private AttrMapper attrMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "为已创建应用补充新的私有属性";
    }

    @Override
    public int sort() {
        return 1;
    }

    @Override
    public int executeForCurrentTenant() {
        List<AppVo> appList = appMapper.listAllAppAttr();
        for (AppVo appVo : appList) {
            AttrType[] attrTypeList = AppTypeManager.getAttrList(appVo.getType());
            if (attrTypeList != null && attrTypeList.length > 0) {
                List<AppAttrVo> privateAttrList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(appVo.getAttrList())) {
                    privateAttrList = appVo.getAttrList().stream().filter(d -> d.getIsPrivate().equals(1)).collect(Collectors.toList());
                }
                List<String> projectAttrTypeList = projectMapper.getProjectAppTypeByProjectId(appVo.getProjectId());
                //补充缺少的内部属性
                for (AttrType attrType : attrTypeList) {
                    IAttrValueHandler handler = AttrHandlerFactory.getHandler(attrType.getType());
                    if (handler != null && (handler.getBelong() == null || projectAttrTypeList.stream().anyMatch(d -> d.equalsIgnoreCase(handler.getBelong())))) {
                        if (privateAttrList.stream().noneMatch(d -> d.getType().equalsIgnoreCase(attrType.getType()))) {
                            AppAttrVo appAttrVo = new AppAttrVo();
                            appAttrVo.setName(attrType.getName());
                            appAttrVo.setLabel(attrType.getLabel());
                            appAttrVo.setType(attrType.getType());
                            appAttrVo.setSort(appVo.getAttrList().size() + 1);
                            appAttrVo.setIsRequired(0);
                            appAttrVo.setIsPrivate(1);
                            appAttrVo.setIsActive(0);
                            appAttrVo.setAppId(appVo.getId());
                            appVo.addAppAttr(appAttrVo);
                            attrMapper.insertAppAttr(appAttrVo);
                        }
                    }
                }
                //删除已经不用的内部属性
                Iterator<AppAttrVo> itAttr = privateAttrList.iterator();
                while (itAttr.hasNext()) {
                    AppAttrVo attrVo = itAttr.next();
                    boolean isFound = false;
                    for (AttrType attrType : attrTypeList) {
                        IAttrValueHandler handler = AttrHandlerFactory.getHandler(attrType.getType());
                        if (handler != null && (handler.getBelong() == null || projectAttrTypeList.stream().anyMatch(d -> d.equalsIgnoreCase(handler.getBelong())))) {
                            if (attrType.getType().equalsIgnoreCase(attrVo.getType())) {
                                isFound = true;
                                attrVo.setName(attrType.getName());
                                attrVo.setLabel(attrType.getLabel());
                            }
                        }
                    }
                    if (!isFound) {
                        attrMapper.deleteAppAttrById(attrVo.getId());
                        itAttr.remove();
                    } else {
                        attrMapper.updateAppAttrName(attrVo);
                    }
                }
            }
        }
        return 0;
    }

}
