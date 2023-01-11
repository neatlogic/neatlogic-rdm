/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.objectattr;

import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.dto.ObjectAttrVo;
import codedriver.framework.rdm.exception.ObjectAttrDeleteException;
import codedriver.framework.rdm.exception.ObjectAttrNotFoundException;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.OperationType;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dao.mapper.ProjectSchemaMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.DELETE)
public class DeleteAttrApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

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
    @Description(desc = "删除对象属性接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        ObjectAttrVo objectAttrVo = projectMapper.getAttrById(id);
        if (objectAttrVo == null) {
            throw new ObjectAttrNotFoundException(id);
        }
        if (objectAttrVo.getIsPrivate().equals(0)) {
            throw new ObjectAttrDeleteException(objectAttrVo);
        }
        projectMapper.deleteObjectAttrById(id);
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/object/attr/delete";
    }
}
