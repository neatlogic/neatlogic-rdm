package codedriver.module.rdm.dto;

import codedriver.framework.dto.UserVo;

import java.util.List;

/**
 * @ClassName TaskVo
 * @Description 任务类
 * @Auther r2d2
 * @Date 2019/12/4 18:17
 **/
public class TaskVo {
    private Long id;
    private String uuid;
    private String name;
    private String description;
    private Long descriptionId;

    private Long projectId;
    private String projectUuid;
    private String projectName;

    private Long iterationId;
    private String processUuid;
    private String iterationName;

    private Long processAreaId;
    private String processAreaUuid;
    private String processAreaName;

    private Long parentId;

    private Long statusId;
    private String statusUuid;
    private String statusName;

    private Long priorityId;
    private String priorityUuid;
    private String priorityName;

    private String processAccountIdList;
    public List<UserVo> processAccountList;

    private String startTime;
    private String endTime;

    private String createUser;
    private String createTime;

    private String updateUser;
    private String updateTime;


    private String customFields;
    private List<FieldVo> taskFieldList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(Long descriptionId) {
        this.descriptionId = descriptionId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getIterationId() {
        return iterationId;
    }

    public void setIterationId(Long iterationId) {
        this.iterationId = iterationId;
    }

    public String getProcessUuid() {
        return processUuid;
    }

    public void setProcessUuid(String processUuid) {
        this.processUuid = processUuid;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
    }

    public Long getProcessAreaId() {
        return processAreaId;
    }

    public void setProcessAreaId(Long processAreaId) {
        this.processAreaId = processAreaId;
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public String getProcessAreaName() {
        return processAreaName;
    }

    public void setProcessAreaName(String processAreaName) {
        this.processAreaName = processAreaName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusUuid() {
        return statusUuid;
    }

    public void setStatusUuid(String statusUuid) {
        this.statusUuid = statusUuid;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public String getPriorityUuid() {
        return priorityUuid;
    }

    public void setPriorityUuid(String priorityUuid) {
        this.priorityUuid = priorityUuid;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getProcessAccountIdList() {
        return processAccountIdList;
    }

    public void setProcessAccountIdList(String processAccountIdList) {
        this.processAccountIdList = processAccountIdList;
    }

    public List<UserVo> getProcessAccountList() {
        return processAccountList;
    }

    public void setProcessAccountList(List<UserVo> processAccountList) {
        this.processAccountList = processAccountList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCustomFields() {
        return customFields;
    }

    public void setCustomFields(String customFields) {
        this.customFields = customFields;
    }

    public List<FieldVo> getTaskFieldList() {
        return taskFieldList;
    }

    public void setTaskFieldList(List<FieldVo> taskFieldList) {
        this.taskFieldList = taskFieldList;
    }
}
