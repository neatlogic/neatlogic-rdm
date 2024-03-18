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

import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.fulltextindex.dto.fulltextindex.FullTextIndexTypeVo;
import neatlogic.framework.rdm.dto.*;
import neatlogic.module.rdm.annotation.SaveIssueAudit;
import neatlogic.module.rdm.annotation.SaveIssueAuditParam;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface IssueMapper {
    Long getIssueStatusById(Long id);

    int checkIssueIsExists(Long id);

    List<IssueCountVo> getIssueCountByAppId(IssueVo issueVo);

    IssueRelVo getIssueRel(@Param("fromIssueId") Long fromIssueId, @Param("toIssueId") Long toIssueId);

    List<Long> getRelIssueIdList(@Param("issueId") Long issueId, @Param("relType") String relType, @Param("direction") String direction);

    int checkIssueIsFavorite(@Param("issueId") Long issueId, @Param("userId") String userId);

    List<ProjectVo> getProjectIssueCountByUserId(IssueConditionVo issueConditionVo);

    List<AppVo> getAppIssueCountByProjectIdAndUserId(IssueConditionVo issueConditionVo);

    IssueVo getIssueById(Long id);

    IssueVo getIssueByIdForAudit(Long id);

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

    @SaveIssueAudit
    void insertIssueRel(@SaveIssueAuditParam IssueRelVo issueRelVo);

    void replaceIssueAttr(IssueVo issueVo);

    void clearIssueParentId(Long id);

    @SaveIssueAudit
    void updateIssue(@SaveIssueAuditParam IssueVo issueVo);

    void deleteIssueUserByIssueId(Long issueId);

    void deleteIssueTagByIssueId(Long issueId);

    void deleteIssueById(IssueVo issueVo);

    void deleteIssueFileByFileId(Long fileId);

    @SaveIssueAudit
    void deleteIssueRel(@SaveIssueAuditParam IssueRelVo issueRelVo);

    void deleteFavoriteIssue(@Param("issueId") Long issueId, @Param("userId") String userId);
}
