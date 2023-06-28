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

package neatlogic.module.rdm.aop;

import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.transaction.core.AfterTransactionJob;
import neatlogic.module.rdm.annotation.SaveIssueAudit;
import neatlogic.module.rdm.annotation.SaveIssueAuditParam;
import neatlogic.module.rdm.dao.mapper.AttrMapper;
import neatlogic.module.rdm.dao.mapper.IssueAuditMapper;
import neatlogic.module.rdm.service.IssueService;
import neatlogic.module.rdm.utils.DiffIssue;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class IssueAuditAspect {
    @Resource
    IssueService issueService;
    @Resource
    AttrMapper attrMapper;

    @Resource
    IssueAuditMapper issueAuditMapper;

    class OldIssue {

        private Long issueId;
        private IssueVo oldIssue;

        public Long getIssueId() {
            return issueId;
        }

        public void setIssueId(Long issueId) {
            this.issueId = issueId;
        }

        public IssueVo getOldIssue() {
            return oldIssue;
        }

        public void setOldIssue(IssueVo oldIssue) {
            this.oldIssue = oldIssue;
        }
    }

    @Around("@annotation(saveIssueAudit)")
    public Object saveIssueAudit(ProceedingJoinPoint point, SaveIssueAudit saveIssueAudit) throws Throwable {
        Object[] params = point.getArgs();
        List<OldIssue> issueList = new ArrayList<>();
        if (params != null && params.length > 0) {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                Object param = params[i];
                Annotation[] paramAnn = annotations[i];
                // 参数为空或没有注解，直接检查下一个参数
                if (param == null || paramAnn == null) {
                    continue;
                }
                for (Annotation annotation : paramAnn) {
                    //content参数
                    if (annotation instanceof SaveIssueAuditParam) {
                        if (param instanceof IssueRelVo) {
                            IssueRelVo issueRelVo = (IssueRelVo) param;
                            IssueVo oldFromIssue = issueService.getIssueByIdForAudit(issueRelVo.getFromIssueId());
                            if (oldFromIssue != null) {
                                OldIssue fromIssue = new OldIssue();
                                fromIssue.setIssueId(issueRelVo.getFromIssueId());
                                fromIssue.setOldIssue(oldFromIssue);
                                issueList.add(fromIssue);
                            }
                            IssueVo oldToIssue = issueService.getIssueByIdForAudit(issueRelVo.getToIssueId());
                            if (oldToIssue != null) {
                                OldIssue toIssue = new OldIssue();
                                toIssue.setIssueId(issueRelVo.getToIssueId());
                                toIssue.setOldIssue(oldToIssue);
                                issueList.add(toIssue);
                            }
                            break;
                        } else if (param instanceof IssueVo) {
                            IssueVo issueVo = (IssueVo) param;
                            IssueVo oldIssue = issueService.getIssueByIdForAudit(issueVo.getId());
                            if (oldIssue != null) {
                                OldIssue issue = new OldIssue();
                                issue.setIssueId(issueVo.getId());
                                issue.setOldIssue(oldIssue);
                                issueList.add(issue);
                            }
                        }
                    }
                }
            }
        }
        Object returnValue = point.proceed(params);
        //保存审计记录的逻辑需要等事务真正成功后再执行
        AfterTransactionJob<List<OldIssue>> afterTransactionJob = new AfterTransactionJob<>("SAVE-ISSUE-AUDIT");
        afterTransactionJob.execute(issueList, oldIssueList -> {
            Map<Long, List<AppAttrVo>> AttrCache = new HashMap<>();
            for (OldIssue oldIssue : oldIssueList) {
                IssueVo issueVo = oldIssue.getOldIssue();
                IssueVo newIssue = issueService.getIssueByIdForAudit(oldIssue.getIssueId());
                if (!AttrCache.containsKey(issueVo.getAppId())) {
                    List<AppAttrVo> appAttrList = attrMapper.getAttrByAppId(issueVo.getAppId());
                    //补充页面没有提供的自定义属性
                    for (AppAttrVo appAttrVo : appAttrList) {
                        if (appAttrVo.getIsPrivate().equals(0)) {
                            if (issueVo.getAttr(appAttrVo.getId()) == null) {
                                issueVo.addAttr(new IssueAttrVo(appAttrVo.getId(), issueVo.getId(), appAttrVo.getType(), appAttrVo.getConfig()));
                            } else {
                                issueVo.getAttr(appAttrVo.getId()).setAttrType(appAttrVo.getType()).setConfig(appAttrVo.getConfig());
                            }
                        }
                    }
                    AttrCache.put(issueVo.getAppId(), appAttrList);
                }
                List<IssueAuditVo> auditList = DiffIssue.getDiff(oldIssue.getIssueId(), issueVo, newIssue, AttrCache.get(issueVo.getAppId()));
                if (CollectionUtils.isNotEmpty(auditList)) {
                    for (IssueAuditVo issueAuditVo : auditList) {
                        issueAuditMapper.insertIssueAudit(issueAuditVo);
                    }
                }
            }
        });
        return returnValue;
    }
}
