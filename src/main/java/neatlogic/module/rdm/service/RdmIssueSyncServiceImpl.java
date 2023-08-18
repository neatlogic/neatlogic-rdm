package neatlogic.module.rdm.service;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.dto.plugin.issue.IssueSearchVo;
import neatlogic.framework.dto.plugin.issue.SyncIssueVo;
import neatlogic.framework.plugin.issue.IssueSyncService;
import neatlogic.framework.rdm.dto.IssueConditionVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class RdmIssueSyncServiceImpl implements IssueSyncService {

    private static final Logger logger = LoggerFactory.getLogger(RdmIssueSyncServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getSource() {
        return "rdm";
    }


    /**
     * 实现这个方法，根据需求状态，和项目，搜索出需求列表
     *
     * @param issueSearchVo
     * @return
     */
    @Override
    public List<SyncIssueVo> doSync(IssueSearchVo issueSearchVo) {
        // 项目的ID列表
        List<String> project = issueSearchVo.getProject();
//        List<String> type = issueSearchVo.getType();
        // 需求筛选的状态列表
        List<String> status = issueSearchVo.getStatus();
        Set<Long> statusIdSet = null;
        if (status != null) {
            statusIdSet = status.stream().map(Long::parseLong).collect(Collectors.toSet());
        }
        
        List<SyncIssueVo> retList = new ArrayList<>();
        if (project != null) {

            for (String projectId : project) {
                IssueConditionVo issueVo = new IssueConditionVo();
                issueVo.setProjectId(Long.parseLong(projectId));
                List<Long> idList = issueMapper.searchIssueId(issueVo);
                if (CollectionUtils.isNotEmpty(idList)) {
                    issueVo.setIdList(idList);
                    List<IssueVo> issueList = issueMapper.searchIssue(issueVo);
                    for (IssueVo vo : issueList) {
                        if (statusIdSet != null && !statusIdSet.contains(vo.getStatus())) {
                            continue;
                        }
                        
                        UserVo createUser = userMapper.getUserByUserUuid(vo.getCreateUser());
                        String createUserName = "";
                        if (createUser != null) {
                            createUserName = createUser.getUserName();
                        }
                        
                        SyncIssueVo syncIssueVo = new SyncIssueVo();
                        syncIssueVo.setSourceId(issueSearchVo.getSyncSourceVo().getId());
                        syncIssueVo.setNo(String.valueOf(vo.getId()));
                        syncIssueVo.setName(vo.getName());
                        syncIssueVo.setType(vo.getAppType());
                        syncIssueVo.setStatus(vo.getStatusName());
                        syncIssueVo.setDescription(vo.getContent());
                        syncIssueVo.setHandleUserId(createUserName);
                        
                        syncIssueVo.setIssueCreateTime(vo.getCreateDate());
                        syncIssueVo.setIssueUpdateTime(vo.getCreateDate());
                        syncIssueVo.setIssueLastSyncTime(new Date());
                        syncIssueVo.setIssueCreator(createUserName);
                        syncIssueVo.setIssueUpdateUser(createUserName);
                        syncIssueVo.setIssuePersonIncharge(createUserName);
                        syncIssueVo.setIssueSyncUser(UserContext.get().getUserName());
                        syncIssueVo.setFcd(new Date());
                        syncIssueVo.setFcu(UserContext.get().getUserUuid());
                        syncIssueVo.setLcd(new Date());
                        syncIssueVo.setLcu(UserContext.get().getUserUuid());
                        
                        retList.add(syncIssueVo);
                    }

                }

            }
        }

        return retList;
    }

    @Override
    public JSONObject getQueryParameter(Long id) {
        return null;
    }
}
