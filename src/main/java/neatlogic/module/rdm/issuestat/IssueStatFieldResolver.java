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

package neatlogic.module.rdm.issuestat;

import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.IssueStatFieldVo;
import neatlogic.framework.rdm.enums.AppStatAttrType;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IssueStatFieldResolver {
    @Resource
    private AttrMapper attrMapper;

    /**
     * 一个statKey只能解析到一个数据源：系统字段优先；未声明系统字段时才查自定义属性绑定。
     */
    public IssueStatFieldVo resolve(Long appId, String statKey) {
        AppStatAttrType statAttrType = AppStatAttrType.get(statKey);
        if (statAttrType == null) {
            return null;
        }
        IssueStatFieldVo fieldVo = new IssueStatFieldVo();
        fieldVo.setStatKey(statKey);
        fieldVo.setStatAttrType(statAttrType);
        if (StringUtils.isNotBlank(statAttrType.getSystemField())) {
            fieldVo.setSourceType("system");
            fieldVo.setSystemField(statAttrType.getSystemField());
            return fieldVo;
        }
        AppAttrVo appAttrVo = attrMapper.getAttrByAppIdAndStatKey(appId, statKey);
        if (appAttrVo != null) {
            fieldVo.setSourceType("custom");
            fieldVo.setAppAttr(appAttrVo);
            return fieldVo;
        }
        return null;
    }
}
