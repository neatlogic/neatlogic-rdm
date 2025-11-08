/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.module.rdm.api.iteration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.IterationVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.IterationMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchIterationApi extends PrivateApiComponentBase {

    @Resource
    private IterationMapper iterationMapper;

    @Override
    public String getName() {
        return "查询迭代";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, desc = "common.keyword"),
            @Param(name = "defaultValue", type = ApiParamType.JSONARRAY, desc = "nmtaa.authmanagesearchapi.input.param.desc.defaultvalue"),
            @Param(name = "projectId", type = ApiParamType.LONG, isRequired = true, desc = "term.rdm.projectid"),
            @Param(name = "isOpen", type = ApiParamType.INTEGER, rule = "0,1", desc = "term.rdm.isopen"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "common.currentpage"),
            @Param(name = "pageSize", type = ApiParamType.STRING, desc = "common.rownum")})
    @Output({@Param(explode = BasePageVo.class)})
    @Description(desc = "查询迭代接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        IterationVo iterationVo = JSON.toJavaObject(paramObj, IterationVo.class);
        List<IterationVo> iterationList = null;
        JSONArray defaultValue = iterationVo.getDefaultValue();
        if (CollectionUtils.isNotEmpty(defaultValue)) {
            JSONObject resultObj = new JSONObject();
            List<Long> idList = defaultValue.toJavaList(Long.class);
            iterationList = iterationMapper.getIterationByIdList(idList);
            resultObj.put("tbodyList", iterationList);
            return resultObj;
        }
        int rowNum = iterationMapper.searchIterationCount(iterationVo);
        if (rowNum > 0) {
            iterationVo.setRowNum(rowNum);
            iterationList = iterationMapper.searchIteration(iterationVo);
        }
        return TableResultUtil.getResult(iterationList, iterationVo);
    }

    @Override
    public String getToken() {
        return "/rdm/iteration/search";
    }
}
