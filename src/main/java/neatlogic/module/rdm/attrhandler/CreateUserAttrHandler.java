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
import neatlogic.framework.rdm.enums.SystemAttrType;
import org.springframework.stereotype.Component;

@Component
public class CreateUserAttrHandler implements IAttrValueHandler {
    @Override
    public String getName() {
        return SystemAttrType.CREATE_USER.getValue();
    }

    @Override
    public String getLabel() {
        return SystemAttrType.CREATE_USER.getLabel();
    }

    @Override
    public String getType() {
        return SystemAttrType.CREATE_USER.getType();
    }

    @Override
    public String getImportHelp() {
        return null;
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
    public boolean getAllowImport() {
        return false;
    }

    @Override
    public boolean getIsSystem() {
        return true;
    }


    @Override
    public SearchExpression[] getSupportExpression() {
        return new SearchExpression[]{SearchExpression.LI, SearchExpression.NL, SearchExpression.NOTNULL,
                SearchExpression.NULL};
    }
}
