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

package neatlogic.module.rdm.attrhandler;

import neatlogic.framework.matrix.constvalue.SearchExpression;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.enums.AttrType;
import org.springframework.stereotype.Component;

@Component
public class TimeCostAttrHandler implements IAttrValueHandler {
    @Override
    public String getName() {
        return AttrType.TIMECOST.getValue();
    }

    @Override
    public String getLabel() {
        return AttrType.TIMECOST.getLabel();
    }

    @Override
    public String getType() {
        return AttrType.TIMECOST.getType();
    }

    @Override
    public String getImportHelp() {
        return "请输入正整数";
    }

    @Override
    public boolean getIsPrivate() {
        return true;
    }

    @Override
    public boolean getIsArray() {
        return false;
    }

    @Override
    public SearchExpression[] getSupportExpression() {
        return new SearchExpression[]{SearchExpression.BT, SearchExpression.NOTNULL, SearchExpression.NULL};
    }
}
