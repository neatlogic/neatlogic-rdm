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

package neatlogic.module.rdm.attrhandler;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.matrix.constvalue.SearchExpression;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.enums.AttrType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Component
public class DatetimeAttrHandler implements IAttrValueHandler {
    @Override
    public String getName() {
        return AttrType.DATETIME.getValue();
    }

    @Override
    public String getLabel() {
        return AttrType.DATETIME.getLabel();
    }

    @Override
    public String getType() {
        return AttrType.DATETIME.getType();
    }

    @Override
    public String getImportHelp() {
        return "请输入日期时间，格式:yyyy-MM-dd hh:mm:ss";
    }

    @Override
    public SearchExpression[] getSupportExpression() {
        return new SearchExpression[]{SearchExpression.BT, SearchExpression.NOTNULL, SearchExpression.NULL};
    }


    @Override
    public boolean getIsPrivate() {
        return false;
    }

    @Override
    public boolean getIsArray() {
        return false;
    }

    @Override
    public Object format(Object value, JSONObject config) {
        if (value instanceof Long) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            if (config != null && StringUtils.isNotBlank(config.getString("format"))) {
                sdf.applyPattern(config.getString("format"));
            } else {
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            }
            return sdf.format(value);
        } else if (value instanceof Timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            if (config != null && StringUtils.isNotBlank(config.getString("format"))) {
                sdf.applyPattern(config.getString("format"));
            } else {
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            }
            return sdf.format(value);
        }
        return value;
    }
}
