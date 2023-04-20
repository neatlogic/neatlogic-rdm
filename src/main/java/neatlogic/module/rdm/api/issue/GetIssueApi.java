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

package neatlogic.module.rdm.api.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.IssueAttrVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class GetIssueApi extends PrivateApiComponentBase {

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private AppMapper appMapper;

    @Override
    public String getName() {
        return "获取任务信息";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "任务id")})
    @Output({@Param(explode = IssueVo.class)})
    @Description(desc = "获取任务信息接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long id = paramObj.getLong("id");
        IssueVo issueVo = issueMapper.getIssueById(id);
        if (issueVo != null) {
            List<AppAttrVo> attrList = appMapper.getAttrByAppId(issueVo.getAppId());
            for (AppAttrVo attr : attrList) {
                if (attr.getIsPrivate().equals(0)) {
                    issueVo.addAttr(new IssueAttrVo(attr.getId(), attr.getType()));
                }
            }
            List<Long> idList = new ArrayList<>();
            idList.add(issueVo.getId());
            issueVo.setIdList(idList);
            HashMap<String, ?> attrMap = issueMapper.getAttrByIssueId(issueVo);
            if (MapUtils.isNotEmpty(attrMap)) {
                List<IssueAttrVo> issueAttrList = new ArrayList<>();
                for (String key : attrMap.keySet()) {
                    if (!key.equals("issueId")) {
                        IssueAttrVo issueAttrVo = new IssueAttrVo();
                        issueAttrVo.setIssueId(issueVo.getId());
                        issueAttrVo.setAttrId(Long.parseLong(key));
                        if (attrMap.get(key).toString().startsWith("[") && attrMap.get(key).toString().endsWith("]")) {
                            issueAttrVo.setValueList(JSONArray.parseArray(attrMap.get(key).toString()));
                        } else {
                            issueAttrVo.setValueList(new JSONArray() {{
                                this.add(attrMap.get(key));
                            }});
                        }
                        issueAttrList.add(issueAttrVo);
                    }
                }
                issueVo.setAttrList(issueAttrList);
            }
        }


        return issueVo;
    }

    @Override
    public String getToken() {
        return "/rdm/issue/get";
    }
}
