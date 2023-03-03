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

package neatlogic.module.rdm.api.objectattr;

import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ObjectAttrVo;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.exception.InsertAttrToSchemaException;
import neatlogic.framework.rdm.exception.ObjectAttrNameIsExistsException;
import neatlogic.framework.rdm.exception.ObjectAttrNotFoundException;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.framework.util.RegexUtils;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.dao.mapper.ProjectSchemaMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class SaveAttrApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectSchemaMapper projectSchemaMapper;

    @Override
    public String getName() {
        return "保存对象属性";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "属性id，不提供代表新增"),
            @Param(name = "objectId", type = ApiParamType.LONG, desc = "对象id", isRequired = true),
            @Param(name = "name", type = ApiParamType.REGEX, rule = RegexUtils.ENCHAR, desc = "唯一标识", isRequired = true, maxLength = 50),
            @Param(name = "label", type = ApiParamType.STRING, desc = "名称", isRequired = true, maxLength = 50),
            @Param(name = "type", type = ApiParamType.ENUM, desc = "类型", isRequired = true, member = AttrType.class),
            @Param(name = "isActive", type = ApiParamType.INTEGER, defaultValue = "0", desc = "是否激活", rule = "1,0"),
            @Param(name = "isRequired", type = ApiParamType.INTEGER, defaultValue = "0", desc = "是否必填", rule = "1,0"),
            @Param(name = "description", type = ApiParamType.STRING, desc = "说明", maxLength = 500)
    })
    @Output({@Param(name = "id", desc = "属性id", type = ApiParamType.LONG)})
    @Description(desc = "保存对象属性接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        ObjectAttrVo objectAttrVo = JSONObject.toJavaObject(paramObj, ObjectAttrVo.class);
        objectAttrVo.setIsPrivate(0);
        if (projectMapper.checkAttrNameIsExists(objectAttrVo) > 0) {
            throw new ObjectAttrNameIsExistsException(objectAttrVo.getName());
        }
        Long id = paramObj.getLong("id");
        if (id == null) {
            int maxSort = projectMapper.getMaxObjectAttrSortByObjectId(objectAttrVo.getObjectId());
            objectAttrVo.setSort(maxSort + 1);
            projectMapper.insertObjectAttr(objectAttrVo);
            EscapeTransactionJob.State s = new EscapeTransactionJob(() -> projectSchemaMapper.insertObjectTableAttr(objectAttrVo.getProjectTableName(), objectAttrVo)).execute();
            if (!s.isSucceed()) {
                throw new InsertAttrToSchemaException(objectAttrVo.getName());
            }
        } else {
            ObjectAttrVo oldObjectAttrVo = projectMapper.getAttrById(id);
            if (oldObjectAttrVo == null) {
                throw new ObjectAttrNotFoundException(id);
            }
            projectMapper.updateObjectAttr(objectAttrVo);
        }
        return objectAttrVo.getId();
    }

    @Override
    public String getToken() {
        return "/rdm/project/object/attr/save";
    }
}
