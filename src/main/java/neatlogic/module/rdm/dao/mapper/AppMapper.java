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

package neatlogic.module.rdm.dao.mapper;

import neatlogic.framework.rdm.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppMapper {
    AppVo getAppByProjectIdAndType(@Param("projectId") Long projectId, @Param("type") String type);

    AppStatusRelVo getAppStatusRel(AppStatusRelVo appStatusRelVo);


    List<AppStatusRelVo> getStatusRelByAppId(Long appId);

    List<AppVo> listAllAppAttr();

    AppVo getAppById(Long id);

    AppUserSettingVo getAppUserSetting(@Param("userId") String userId, @Param("appId") Long appId);

    List<AppVo> getIterationAppType(Long iterationId);

    List<AppVo> getAppDetailByProjectId(@Param("projectId") Long projectId, @Param("isActive") Integer isActive);

    AppStatusVo getStatusById(Long id);

    AppStatusVo getStatusByAppIdAndName(@Param("appId") Long appId, @Param("name") String name);

    List<AppStatusVo> getStatusByAppId(IssueVo issueVo);


    void updateAppStatus(AppStatusVo appStatusVo);

    void updateAppSort(AppVo appVo);

    void updateAppStatusSort(AppStatusVo appStatusVo);

    void saveAppConfig(AppVo appVo);

    void updateAppStatusRelConfig(AppStatusRelVo appStatusRelVo);

    void resetAppStatusIsStart(Long appId);

    void updateAppStatusType(AppStatusVo appStatusVo);

    void updateAppIsActive(@Param("appId") Long appId, @Param("isActive") Integer isActive);

    void insertAppStatusRel(AppStatusRelVo appStatusRelVo);

    void insertApp(AppVo appVo);

    void insertAppStatus(AppStatusVo appStatusVo);

    void insertAppUserSetting(AppUserSettingVo appUserSettingVo);


    void deleteAppById(Long id);

    void deleteAppStatusRel(AppStatusRelVo appStatusRelVo);


    void deleteAppStatusById(Long id);

}
