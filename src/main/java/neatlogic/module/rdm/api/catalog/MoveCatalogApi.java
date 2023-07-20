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

package neatlogic.module.rdm.api.catalog;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.lrcode.LRCodeManager;
import neatlogic.framework.lrcode.constvalue.MoveType;
import neatlogic.framework.lrcode.exception.MoveTargetNodeIllegalException;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppCatalogVo;
import neatlogic.framework.rdm.exception.CatalogNotFoundException;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.CatalogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
public class MoveCatalogApi extends PrivateApiComponentBase {

    @Resource
    private CatalogMapper catalogMapper;

    @Override
    public String getName() {
        return "移动目录";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id"),
            @Param(name = "id", type = ApiParamType.LONG, isRequired = true, desc = "被移动的目录id"),
            @Param(name = "targetId", type = ApiParamType.LONG, isRequired = true, desc = "目标目录id"),
            @Param(name = "moveType", type = ApiParamType.ENUM, rule = "inner,prev,next", isRequired = true, desc = "移动类型")})
    @Description(desc = "移动目录接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        Long appId = paramObj.getLong("appId");
        //目标节点uuid
        Long targetId = paramObj.getLong("targetId");
        if (id.equals(targetId)) {
            throw new MoveTargetNodeIllegalException();
        }
        AppCatalogVo source = catalogMapper.getAppCatalogById(id);
        //判断被移动的服务通道是否存在
        if (source == null) {
            throw new CatalogNotFoundException(id);
        }
        AppCatalogVo target = catalogMapper.getAppCatalogById(targetId);
        if (target == null) {
            throw new CatalogNotFoundException(targetId);
        }
        MoveType moveType = MoveType.getMoveType(paramObj.getString("moveType"));
        if (MoveType.INNER == moveType) {
            source.setParentId(target.getId());
        } else if (MoveType.PREV == moveType) {
            source.setParentId(target.getParentId());
            LRCodeManager.moveTreeNode("rdm_app_catalog", "id", "parent_id", source.getId(), moveType, target.getId(), "app_id = " + appId);
        } else if (MoveType.NEXT == moveType) {
            source.setParentId(target.getParentId());
            LRCodeManager.moveTreeNode("rdm_app_catalog", "id", "parent_id", source.getId(), moveType, target.getId(), "app_id = " + appId);
        }
        catalogMapper.updateAppCatalogParentId(source);

        LRCodeManager.rebuildLeftRightCode("rdm_app_catalog", "id", "parent_id", "app_id = " + appId);
        return null;

    }

    @Override
    public String getToken() {
        return "/rdm/catalog/move";
    }
}
