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

package neatlogic.module.rdm.service;

import com.alibaba.fastjson.JSONArray;
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.rdm.dto.IssueAttrVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private AppMapper appMapper;

    @Resource
    private AttrMapper attrMapper;


    @Override
    public IssueVo getIssueById(Long id) {
        IssueVo issueVo = issueMapper.getIssueById(id);
        if (issueVo != null) {
            List<AppAttrVo> attrList = attrMapper.getAttrByAppId(issueVo.getAppId());
            for (AppAttrVo attr : attrList) {
                if (attr.getIsPrivate().equals(0)) {
                    issueVo.addAppAttr(attr);
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
                        Long attrId = Long.parseLong(key);
                        Optional<AppAttrVo> op = attrList.stream().filter(d -> d.getId().equals(attrId)).findFirst();
                        if (op.isPresent()) {
                            AppAttrVo appAttrVo = op.get();
                            IssueAttrVo issueAttrVo = new IssueAttrVo();
                            issueAttrVo.setIssueId(issueVo.getId());
                            issueAttrVo.setAttrId(attrId);
                            issueAttrVo.setAttrType(appAttrVo.getType());
                            issueAttrVo.setConfig(appAttrVo.getConfig());
                            if (attrMap.get(key).toString().startsWith("[") && attrMap.get(key).toString().endsWith("]")) {
                                JSONArray valueList = JSONArray.parseArray(attrMap.get(key).toString());
                                issueAttrVo.setValueList(valueList);
                            } else {
                                issueAttrVo.setValueList(new JSONArray() {{
                                    this.add(attrMap.get(key));
                                }});
                            }
                            issueAttrList.add(issueAttrVo);
                        }
                    }
                }
                issueVo.setAttrList(issueAttrList);
            }
        }
        return issueVo;
    }
}
