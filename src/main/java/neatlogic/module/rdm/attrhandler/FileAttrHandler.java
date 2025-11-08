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

import neatlogic.framework.matrix.constvalue.SearchExpression;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.enums.AttrType;
import org.springframework.stereotype.Component;

@Component
public class FileAttrHandler implements IAttrValueHandler {
    @Override
    public String getName() {
        return AttrType.FILE.getValue();
    }

    @Override
    public String getLabel() {
        return AttrType.FILE.getLabel();
    }

    @Override
    public String getType() {
        return AttrType.FILE.getType();
    }

    @Override
    public String getImportHelp() {
        return null;
    }

    @Override
    public boolean getAllowImport() {
        return false;
    }

    @Override
    public boolean getAllowSort() {
        return false;
    }

    @Override
    public boolean getIsPrivate() {
        return false;
    }

    @Override
    public boolean getIsArray() {
        return true;
    }

    @Override
    public SearchExpression[] getSupportExpression() {
        return new SearchExpression[]{SearchExpression.NOTNULL, SearchExpression.NULL};
    }

}
