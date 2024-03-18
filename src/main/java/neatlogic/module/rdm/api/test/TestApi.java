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
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(attrType.getType());
            System.out.println(handler.getName().toUpperCase() + "(\"" + handler.getType() + "\", \"" + handler.getName() + "\", \"" + handler.getLabel() + "\", " + handler.getIsPrivate() + ", " + handler.getIsArray() + ", " + handler.getBelong() + ", \"" + handler.getImportHelp() + "\"),");
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/test";
    }
}
