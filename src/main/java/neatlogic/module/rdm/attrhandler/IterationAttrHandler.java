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

package neatlogic.module.rdm.attrhandler;

import neatlogic.framework.matrix.constvalue.SearchExpression;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.enums.AttrType;
import org.springframework.stereotype.Component;

@Component
public class IterationAttrHandler implements IAttrValueHandler {
    @Override
    public String getName() {
        return AttrType.ITERATION.getValue();
    }

    @Override
    public String getLabel() {
        return AttrType.ITERATION.getLabel();
    }

    @Override
    public String getType() {
        return AttrType.ITERATION.getType();
    }

    @Override
    public String getImportHelp() {
        return "请输入迭代名称";
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
    public String getBelong() {
        return "iteration";
    }

    @Override
    public SearchExpression[] getSupportExpression() {
        return new SearchExpression[]{SearchExpression.EQ, SearchExpression.NE, SearchExpression.LI,
                SearchExpression.NL, SearchExpression.NOTNULL, SearchExpression.NULL};
    }
}
