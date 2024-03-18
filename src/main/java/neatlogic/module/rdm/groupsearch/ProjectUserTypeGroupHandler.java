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

package neatlogic.module.rdm.groupsearch;

import neatlogic.framework.rdm.dto.ProjectUserVo;
import neatlogic.framework.rdm.enums.IssueGroupSearch;
import neatlogic.framework.rdm.enums.ProjectUserType;
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
public class ProjectUserTypeGroupHandler implements IGroupSearchHandler {
    @Override
    public String getName() {
        return IssueGroupSearch.PROJECTUSERTYPE.getValue();
    }

    @Override
    public String getLabel() {
        return IssueGroupSearch.PROJECTUSERTYPE.getText();
    }

    @Resource
    private ProjectMapper projectMapper;


    @Override
    public List<GroupSearchOptionVo> search(GroupSearchVo groupSearchVo) {
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

        return convertGroupSearchOption(projectMapper.searchProjectUser(userVo));
    }

    @Override
    public List<GroupSearchOptionVo> reload(GroupSearchVo groupSearchVo) {
        Long projectId = groupSearchVo.getProjectId();
        List<ProjectUserVo> userList = new ArrayList<>();
        if (projectId == null) {
            return new ArrayList<>();
        }
        List<String> userUuidList = new ArrayList<>();
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
        return convertGroupSearchOption(userList);
    }

    private List<GroupSearchOptionVo> convertGroupSearchOption(List<ProjectUserVo> userList) {
        List<GroupSearchOptionVo> dataList = new ArrayList<>();
        for (ProjectUserVo userVo : userList) {
            GroupSearchOptionVo groupSearchOptionVo = new GroupSearchOptionVo();
            groupSearchOptionVo.setValue(getHeader() + userVo.getUserId());
            groupSearchOptionVo.setText(userVo.getUserName() + "[" + userVo.getUserEnName() + "]");
            dataList.add(groupSearchOptionVo);
        }
        return dataList;
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
