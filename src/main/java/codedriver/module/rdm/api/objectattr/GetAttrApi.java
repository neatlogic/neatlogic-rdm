/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.objectattr;

import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.dto.ObjectAttrVo;
import codedriver.framework.restful.annotation.*;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetAttrApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "获取对象属性信息";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "属性id", isRequired = true, type = ApiParamType.LONG)})
    @Output({@Param(explode = ObjectAttrVo.class)})
    @Description(desc = "获取对象属性信息接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        return projectMapper.getAttrById(paramObj.getLong("id"));
    }

    @Override
    public String getToken() {
        return "/rdm/project/object/attr/get";
    }
}
