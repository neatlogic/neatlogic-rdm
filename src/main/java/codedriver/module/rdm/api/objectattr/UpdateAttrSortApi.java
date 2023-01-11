/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.objectattr;

import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.dto.ObjectAttrVo;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.OperationType;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class UpdateAttrSortApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "更新对象属性顺序";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "idList", type = ApiParamType.JSONARRAY, desc = "属性id列表"),
            @Param(name = "objectId", type = ApiParamType.LONG, desc = "对象id", isRequired = true)
    })
    @Description(desc = "更新对象属性顺序接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        JSONArray idList = paramObj.getJSONArray("idList");
        if (CollectionUtils.isNotEmpty(idList)) {
            for (int i = 0; i < idList.size(); i++) {
                ObjectAttrVo objectAttrVo = new ObjectAttrVo();
                objectAttrVo.setSort(i + 1);
                objectAttrVo.setId(idList.getLong(i));
                projectMapper.updateObjectAttrSort(objectAttrVo);
            }
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/object/attr/updatesort";
    }
}
