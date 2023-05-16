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

package neatlogic.module.rdm.api.appattr;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.exception.AppAttrDeleteException;
import neatlogic.framework.rdm.exception.AppAttrNotFoundException;
import neatlogic.framework.rdm.exception.DeleteAttrSchemaException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.transaction.core.EscapeTransactionJob;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.ProjectSchemaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.DELETE)
@Transactional
public class DeleteAttrApi extends PrivateApiComponentBase {


    @Resource
    private AttrMapper attrMapper;
    @Resource
    private ProjectSchemaMapper projectSchemaMapper;

    @Override
    public String getName() {
        return "删除对象属性";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "属性id", isRequired = true)})
    @Description(desc = "删除应用属性接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        AppAttrVo objectAttrVo = attrMapper.getAttrById(id);
        if (objectAttrVo == null) {
            throw new AppAttrNotFoundException(id);
        }
        if (objectAttrVo.getIsPrivate().equals(1)) {
            throw new AppAttrDeleteException(objectAttrVo);
        }
        attrMapper.deleteAppAttrById(id);
        EscapeTransactionJob.State s = new EscapeTransactionJob(() -> projectSchemaMapper.deleteAppTableAttr(objectAttrVo.getTableName(), objectAttrVo)).execute();
        if (!s.isSucceed()) {
            throw new DeleteAttrSchemaException(objectAttrVo.getName());
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/app/attr/delete";
    }
}
