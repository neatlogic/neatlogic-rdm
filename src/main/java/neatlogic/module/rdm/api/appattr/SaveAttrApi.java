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

package neatlogic.module.rdm.api.appattr;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.exception.AppAttrNameIsExistsException;
import neatlogic.framework.rdm.exception.AppAttrNotFoundException;
import neatlogic.framework.rdm.exception.InsertAttrToSchemaException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.framework.util.RegexUtils;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.ProjectSchemaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveAttrApi extends PrivateApiComponentBase {
    @Resource
    private AttrMapper attrMapper;


    @Resource
    private ProjectSchemaMapper projectSchemaMapper;

    @Override
    public String getName() {
        return "nmraa.saveattrapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "nmraa.saveattrapi.input.param.desc.id"),
            @Param(name = "appId", type = ApiParamType.LONG, desc = "nmraa.getappapi.input.param.desc", isRequired = true),
            @Param(name = "name", type = ApiParamType.REGEX, rule = RegexUtils.ENCHAR, desc = "common.uniquename", isRequired = true, maxLength = 50),
            @Param(name = "label", type = ApiParamType.STRING, desc = "common.name", isRequired = true, maxLength = 50),
            @Param(name = "type", type = ApiParamType.ENUM, desc = "common.type", isRequired = true, member = AttrType.class),
            @Param(name = "isActive", type = ApiParamType.INTEGER, defaultValue = "0", desc = "common.isactive", rule = "1,0"),
            @Param(name = "isRequired", type = ApiParamType.INTEGER, defaultValue = "0", desc = "common.isrequired", rule = "1,0"),
            @Param(name = "description", type = ApiParamType.STRING, desc = "common.description", maxLength = 500)
    })
    @Output({@Param(name = "id", desc = "nmcaa.getattrapi.input.param.desc.id", type = ApiParamType.LONG)})
    @Description(desc = "nmraa.saveattrapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppAttrVo appAttrVo = JSONObject.toJavaObject(paramObj, AppAttrVo.class);
        appAttrVo.setIsPrivate(0);
        if (attrMapper.checkAttrNameIsExists(appAttrVo) > 0) {
            throw new AppAttrNameIsExistsException(appAttrVo.getName());
        }
        Long id = paramObj.getLong("id");
        if (id == null) {
            Integer maxSort = attrMapper.getMaxAppAttrSortByAppId(appAttrVo.getAppId());
            maxSort = maxSort == null ? 0 : maxSort;
            appAttrVo.setSort(maxSort + 1);
            attrMapper.insertAppAttr(appAttrVo);
            EscapeTransactionJob.State s = new EscapeTransactionJob(() -> projectSchemaMapper.insertAppTableAttr(appAttrVo.getTableName(), appAttrVo)).execute();
            if (!s.isSucceed()) {
                throw new InsertAttrToSchemaException(appAttrVo.getName());
            }
        } else {
            AppAttrVo oldAppAttrVo = attrMapper.getAttrById(id);
            if (oldAppAttrVo == null) {
                throw new AppAttrNotFoundException(id);
            }
            attrMapper.updateAppAttr(appAttrVo);
        }
        return appAttrVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/attr/save";
    }
}
