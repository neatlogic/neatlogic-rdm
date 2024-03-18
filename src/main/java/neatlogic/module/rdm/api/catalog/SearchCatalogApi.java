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

package neatlogic.module.rdm.api.catalog;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppCatalogVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.CatalogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchCatalogApi extends PrivateApiComponentBase {

    @Resource
    private CatalogMapper catalogMapper;

    @Override
    public String getName() {
        return "查询目录";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "appId", type = ApiParamType.LONG, isRequired = true, desc = "应用id"),
            @Param(name = "needCheckIsInUsed", type = ApiParamType.BOOLEAN, desc = "是否需要计算是否被关联")
    })
    @Output({@Param(explode = AppCatalogVo[].class)})
    @Description(desc = "查询目录接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        AppCatalogVo appCatalogVo = JSONObject.toJavaObject(paramObj, AppCatalogVo.class);

        List<AppCatalogVo> catalogList = catalogMapper.searchAppCatalog(appCatalogVo);
        List<AppCatalogVo> newCatalogList = new ArrayList<>();
        Map<Long, AppCatalogVo> catalogMap = new HashMap<>();
        for (AppCatalogVo catalogVo : catalogList) {
            catalogMap.put(catalogVo.getId(), catalogVo);
        }
        for (AppCatalogVo catalogVo : catalogList) {
            if (catalogVo.getParentId() == null) {
                newCatalogList.add(catalogVo);
            } else {
                AppCatalogVo parentCatalog = catalogMap.get(catalogVo.getParentId());
                if (parentCatalog != null) {
                    parentCatalog.setExpand(true);
                    parentCatalog.addChild(catalogVo);
                }
            }
        }
        return newCatalogList;
    }

    @Override
    public String getToken() {
        return "/rdm/catalog/search";
    }
}
