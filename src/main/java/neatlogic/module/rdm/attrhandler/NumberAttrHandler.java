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
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class NumberAttrHandler implements IAttrValueHandler {
    @Override
    public String getName() {
        return AttrType.NUMBER.getValue();
    }

    @Override
    public String getLabel() {
        return AttrType.NUMBER.getLabel();
    }

    @Override
    public String getType() {
        return AttrType.NUMBER.getType();
    }

    @Override
    public String getImportHelp() {
        return "请输入合法的数字";
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
        //确保数字的精度一致，例如1.000和1应该是相等
        DecimalFormat df = new DecimalFormat("#.####");
        try {
            return df.format(value);
        } catch (Exception ex) {
            return value;
        }
    }

    @Override
    public SearchExpression[] getSupportExpression() {
        return new SearchExpression[]{SearchExpression.BT, SearchExpression.NOTNULL, SearchExpression.NULL};
    }
}
