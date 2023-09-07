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

package neatlogic.module.rdm.api.test;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.rdm.attrhandler.code.AttrHandlerFactory;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class TestApi extends PrivateApiComponentBase {


    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Description(desc = "test")
    @Override
    public Object myDoService(JSONObject paramObj) {
        for (AttrType attrType : AttrType.values()) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(attrType.getValue());
            System.out.println(handler.getName().toUpperCase() + "(\"" + handler.getType() + "\", \"" + handler.getName() + "\", \"" + handler.getLabel() + "\", " + handler.getIsPrivate() + ", " + handler.getIsArray() + ", " + handler.getBelong() + ", \"" + handler.getImportHelp() + "\"),");
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/test";
    }
}
