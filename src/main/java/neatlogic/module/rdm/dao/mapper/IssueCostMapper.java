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

import neatlogic.framework.rdm.dto.IssueCostVo;

import java.util.List;

public interface IssueCostMapper {
    List<IssueCostVo> searchIssueCost(IssueCostVo issueCostVo);

    int searchIssueCostCount(IssueCostVo issueCostVo);

    IssueCostVo getIssueCostById(Long id);

    void updateIssueCost(IssueCostVo issueCostVo);

    void insertIssueCost(IssueCostVo issueCostVo);

    void deleteIssueCost(Long id);
}