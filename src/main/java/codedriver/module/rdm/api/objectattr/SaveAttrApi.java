/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.objectattr;

import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.dto.ObjectAttrVo;
import codedriver.framework.rdm.enums.AttrType;
import codedriver.framework.rdm.exception.InsertAttrToSchemaException;
import codedriver.framework.rdm.exception.ObjectAttrNameIsExistsException;
import codedriver.framework.rdm.exception.ObjectAttrNotFoundException;
import codedriver.framework.restful.annotation.*;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.framework.transaction.core.EscapeTransactionJob;
import codedriver.framework.util.RegexUtils;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dao.mapper.ProjectSchemaMapper;
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
