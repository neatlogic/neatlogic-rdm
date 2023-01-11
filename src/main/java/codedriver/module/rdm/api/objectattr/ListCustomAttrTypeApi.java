/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.objectattr;

import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.enums.AttrType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.OperationType;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListCustomAttrTypeApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "获取对象属性类型列表";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Output({@Param(explode = ValueTextVo[].class)})
    @Description(desc = "获取对象属性类型列表接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        List<ValueTextVo> dataList = new ArrayList<>();
        for (AttrType attrType : AttrType.values()) {
            if (attrType.getAllowCustom().equals(1)) {
                dataList.add(new ValueTextVo(attrType.getName(), attrType.getLabel()));
            }
        }
        return dataList;
    }

    @Override
    public String getToken() {
        return "/rdm/project/object/customattrtype/list";
    }
}
