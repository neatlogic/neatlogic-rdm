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

import com.alibaba.fastjson.JSONObject;
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
    public Object format(Object value, JSONObject config) {
        //确保数字的精度一致，例如1.000和1应该是相等
        DecimalFormat df = new DecimalFormat("#.####");
        try {
            return df.format(value);
        } catch (Exception ex) {
            return value;
        }
    }
}
