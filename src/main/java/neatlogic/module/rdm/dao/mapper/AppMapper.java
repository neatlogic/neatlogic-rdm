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

package neatlogic.module.rdm.dao.mapper;

import neatlogic.framework.rdm.dto.*;

import java.util.List;

public interface AppMapper {
    int checkAppCatalogNameIsExists(AppCatalogVo appCatalogVo);

    AppCatalogVo getAppCatalogById(Long id);

    List<AppCatalogVo> searchAppCatalog(AppCatalogVo appCatalogVo);

    AppAttrVo getAttrById(Long attrId);

    int getMaxAppAttrSortByAppId(Long appId);

    List<AppStatusRelVo> getStatusRelByAppId(Long appId);

    List<AppAttrVo> searchAppAttr(AppAttrVo appAttrVo);

    AppVo getAppById(Long id);

    List<AppVo> getAppDetailByProjectId(Long projectId);

    AppStatusVo getStatusById(Long id);

    List<AppStatusVo> getStatusByAppId(Long objectId);

    void updateAppCatalogParentId(AppCatalogVo appCatalogVo);

    void updateAppCatalogLft(AppCatalogVo appCatalogVo);

    void updateAppAttr(AppAttrVo appAttrVo);

    void updateAppAttrIsActive(AppAttrVo appAttrVo);

    void updateAppStatus(AppStatusVo appStatusVo);

    void updateAppAttrSort(AppAttrVo appAttrVo);

    void updateAppCatalog(AppCatalogVo appCatalogVo);

    void insertAppCatalog(AppCatalogVo appCatalogVo);

    void insertApp(AppVo appVo);

    void insertAppStatus(AppStatusVo appStatusVo);

    void insertAppAttr(AppAttrVo appAttrVo);

    void deleteAppById(Long id);

    void deleteAppAttrById(Long id);

}
