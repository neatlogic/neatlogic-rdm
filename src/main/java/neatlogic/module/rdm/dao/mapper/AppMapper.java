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

    /**
     * 查询指定App类型使用过的状态标签并去重。
     */
    List<String> getStatusLabelListByAppType(String appType);

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
