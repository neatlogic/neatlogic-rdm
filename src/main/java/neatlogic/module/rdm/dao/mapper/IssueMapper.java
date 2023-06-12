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

import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.fulltextindex.dto.fulltextindex.FullTextIndexTypeVo;
import neatlogic.framework.rdm.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface IssueMapper {
    int checkIssueIsFavorite(@Param("issueId") Long issueId, @Param("userId") String userId);

    List<ProjectVo> getProjectIssueCountByUserId(IssueConditionVo issueConditionVo);

    List<AppVo> getAppIssueCountByProjectIdAndUserId(IssueConditionVo issueConditionVo);

    IssueVo getIssueById(Long id);

    List<AppIssueCountVo> getIssueCountByIterationId(@Param("iterationId") Long iterationId);

    Long getIssueIdByFileId(Long fileId);

    List<FileVo> getIssueFileByIssueId(Long issueId);

    List<Long> getNotIndexIssueIdList(FullTextIndexTypeVo typeVo);

    List<TagVo> getTagByIssueId(Long issueId);

    List<IssueCountVo> getIssueCountByProjectId(IssueCountVo issueCountVo);

    List<IssueVo> searchIssue(IssueVo issueVo);

    List<Long> searchIssueId(IssueVo issueVo);

    List<HashMap<String, ?>> getAttrByIssueIdList(IssueVo issueVo);

    HashMap<String, ?> getAttrByIssueId(IssueVo issueVo);

    int searchIssueCount(IssueVo issueVo);

    void updateIssueAttr(IssueVo issueVo);

    void deleteIssueByAppId(AppVo appVo);

    void insertIssue(IssueVo issueVo);

    void insertIssueUser(@Param("issueId") Long issueId, @Param("userId") String userId);

    void insertIssueFile(@Param("issueId") Long issueId, @Param("fileId") Long fileId);

    void insertIssueIsFavorite(@Param("issueId") Long issueId, @Param("userId") String userId);

    void insertIssueTag(@Param("issueId") Long issueId, @Param("tagId") Long tagId);

    void insertIssueRel(IssueRelVo issueRelVo);

    void replaceIssueAttr(IssueVo issueVo);

    void clearIssueParentId(Long id);

    void updateIssue(IssueVo issueVo);

    void deleteIssueUserByIssueId(Long issueId);

    void deleteIssueTagByIssueId(Long issueId);

    void deleteIssueById(Long issueId);

    void deleteIssueFileByFileId(Long fileId);

    void deleteIssueRel(@Param("fromIssueId") Long fromIssueId, @Param("toIssueId") Long toIssueId);

    void deleteFavoriteIssue(@Param("issueId") Long issueId, @Param("userId") String userId);
}
