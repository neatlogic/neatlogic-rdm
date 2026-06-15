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

package neatlogic.module.rdm.dao.mapper;

import neatlogic.framework.rdm.dto.AppAttrVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttrMapper {
    int checkAttrNameIsExists(AppAttrVo objectAttrVo);

    int checkAttrStatKeyIsExists(AppAttrVo appAttrVo);

    List<AppAttrVo> getAttrByAppId(Long appId);

    AppAttrVo getAttrById(Long attrId);

    Long getAttrIdByAppIdAndName(@Param("appId") Long appId, @Param("name") String name);

    AppAttrVo getAttrByAppIdAndStatKey(@Param("appId") Long appId, @Param("statKey") String statKey);

    Integer getMaxAppAttrSortByAppId(Long appId);

    List<AppAttrVo> searchAppAttr(AppAttrVo appAttrVo);


    void insertAppAttr(AppAttrVo appAttrVo);

    void updateAppAttrName(AppAttrVo appAttrVo);

    void updateAppAttr(AppAttrVo appAttrVo);

    void updateAppAttrIsActive(AppAttrVo appAttrVo);

    void updateAppAttrIsRequired(AppAttrVo appAttrVo);

    void updateAppAttrSort(AppAttrVo appAttrVo);

    void deleteAppAttrById(Long id);
}
