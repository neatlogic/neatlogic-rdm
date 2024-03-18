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

package neatlogic.module.rdm.api.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ProjectUserVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.Input;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.UPDATE)
@Transactional
public class SaveProjectUserApi extends PrivateApiComponentBase {
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "nmrap.saveprojectuserapi.getname";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "projectId", type = ApiParamType.LONG, desc = "term.rdm.projectid", isRequired = true),
            @Param(name = "userType", type = ApiParamType.STRING, desc = "common.usertype", isRequired = true),
            @Param(name = "userIdList", type = ApiParamType.JSONARRAY, desc = "common.userlist", isRequired = true)})
    @Description(desc = "nmrap.saveprojectuserapi.getname")
    @Override
    public Object myDoService(JSONObject paramObj) {
        JSONArray userIdList = paramObj.getJSONArray("userIdList");
        String userType = paramObj.getString("userType");
        Long projectId = paramObj.getLong("projectId");
        for (int i = 0; i < userIdList.size(); i++) {
            String userId = userIdList.getString(i).replace("user#", "");
            ProjectUserVo projectUserVo = new ProjectUserVo();
            projectUserVo.setUserId(userId);
            projectUserVo.setProjectId(projectId);
            projectUserVo.setUserType(userType);
            projectMapper.insertProjectUser(projectUserVo);
        }
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/user/save";
    }
}
