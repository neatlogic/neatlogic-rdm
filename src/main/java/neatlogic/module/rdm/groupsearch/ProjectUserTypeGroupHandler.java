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

package neatlogic.module.rdm.groupsearch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.UserType;
import neatlogic.framework.rdm.dto.ProjectUserVo;
import neatlogic.framework.rdm.enums.IssueGroupSearch;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.restful.groupsearch.core.GroupSearchGroupVo;
import neatlogic.framework.restful.groupsearch.core.GroupSearchOptionVo;
import neatlogic.framework.restful.groupsearch.core.GroupSearchVo;
import neatlogic.framework.restful.groupsearch.core.IGroupSearchHandler;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectUserTypeGroupHandler implements IGroupSearchHandler<ProjectUserVo> {
    @Override
    public String getName() {
        return IssueGroupSearch.PROJECTUSERTYPE.getValue();
    }

    @Resource
    private ProjectMapper projectMapper;


    @Override
    public List<ProjectUserVo> search(GroupSearchVo groupSearchVo) {
//        Long projectId = jsonObj.getLong("projectId");
        Long projectId = groupSearchVo.getProjectId();
        if (projectId == null) {
            return new ArrayList<>();
        }
        ProjectUserVo userVo = new ProjectUserVo();
        userVo.setNeedPage(true);
        userVo.setPageSize(20);
        userVo.setCurrentPage(1);
        userVo.setUserType(ProjectUserType.MEMBER.getValue());
        userVo.setProjectId(projectId);
        userVo.setKeyword(groupSearchVo.getKeyword());
//        userVo.setKeyword(jsonObj.getString("keyword"));

        return projectMapper.searchProjectUser(userVo);
    }

    @Override
    public List<ProjectUserVo> reload(GroupSearchVo groupSearchVo) {
//        Long projectId = jsonObj.getLong("projectId");
        Long projectId = groupSearchVo.getProjectId();
        List<ProjectUserVo> userList = new ArrayList<>();
        if (projectId == null) {
            return userList;
        }
        List<String> userUuidList = new ArrayList<>();
//        List<String> valueList = JSONObject.parseArray(jsonObj.getJSONArray("valueList").toJSONString(), String.class);
//        for (Object value : valueList) {
//            if (value.toString().startsWith(getHeader())) {
//                userUuidList.add(value.toString().replace(getHeader(), ""));
//            }
//        }
        for (String value : groupSearchVo.getValueList()) {
            if (value.startsWith(getHeader())) {
                userUuidList.add(value.replace(getHeader(), StringUtils.EMPTY));
            }
        }
        if (!userUuidList.isEmpty()) {
            ProjectUserVo userVo = new ProjectUserVo();
            userVo.setNeedPage(true);
            userVo.setPageSize(20);
            userVo.setCurrentPage(1);
            userVo.setUserType(ProjectUserType.MEMBER.getValue());
            userVo.setProjectId(projectId);
            userVo.setUserIdList(userUuidList);
            userList = projectMapper.searchProjectUser(userVo);
        }
        return userList;
    }

    @Override
    public GroupSearchGroupVo repack(List<ProjectUserVo> userList) {
        GroupSearchGroupVo groupSearchGroupVo = new GroupSearchGroupVo();
        groupSearchGroupVo.setValue("rdm.project");
        groupSearchGroupVo.setText("项目干系人");
        groupSearchGroupVo.setSort(getSort());
        List<GroupSearchOptionVo> dataList = new ArrayList<>();
        for (ProjectUserVo userVo : userList) {
            GroupSearchOptionVo groupSearchOptionVo = new GroupSearchOptionVo();
            groupSearchOptionVo.setValue(getHeader() + userVo.getUserId());
            groupSearchOptionVo.setText(userVo.getUserName());
            dataList.add(groupSearchOptionVo);
        }
        groupSearchGroupVo.setDataList(dataList);
        return groupSearchGroupVo;
//        JSONObject userTypeObj = new JSONObject();
//        userTypeObj.put("value", "rdm.project");
//        userTypeObj.put("text", "项目干系人");
//        userTypeObj.put("sort", getSort());
//        JSONArray userArray = new JSONArray();
//        for (ProjectUserVo userVo : userList) {
//            JSONObject userTmp = new JSONObject();
//            userTmp.put("value", getHeader() + userVo.getUserId());
//            userTmp.put("text", userVo.getUserName());
//            userArray.add(userTmp);
//        }
//        userTypeObj.put("sort", getSort());
//        userTypeObj.put("dataList", userArray);
//        return userTypeObj;
    }

    @Override
    public int getSort() {
        return 3;
    }

    @Override
    public Boolean isLimit() {
        return false;
    }
}
