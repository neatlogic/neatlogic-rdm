package codedriver.module.rdm.dto;

import codedriver.framework.dto.UserVo;

import java.util.List;

/**
 * @ClassName TaskVo
 * @Description 任务类
 * @Auther
 * @Date 2019/12/4 18:17
 **/
public class TaskVo {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 任务uuid
     */
    private String uuid;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 项目uuid
     */
    private String projectUuid;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 迭代id
     */
    private String iterationUuid;

    /**
     * 迭代名称
     */
    private String iterationName;

    /**
     * 过程域uuid
     */
    private String processAreaUuid;

    /**
     * 过程域名称
     */
    private String processAreaName;

    /**
     * 父任务Uuid
     */
    private String parentUuid;

    /**
     * 状态uuid
     */
    private String statusUuid;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 优先级uuid
     */
    private String priorityUuid;


    /**
     * 优先级名称
     */
    private String priorityName;

    /**
     * 类别Uuid
     */
    private String categoryUuid;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 处理人id集合
     */
    private List<String> processAccountIdList;

    /**
     * 处理人集合
     */
    public List<UserVo> processAccountList;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 该值是一个json数组，为将来数据库扩展准备()
     */
    private String customFields;

    /**
     * 任务属性集合
     */
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


    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
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

    public List<String> getProcessAccountIdList() {
        return processAccountIdList;
    }

    public void setProcessAccountIdList(List<String> processAccountIdList) {
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

    public String getIterationUuid() {
        return iterationUuid;
    }

    public void setIterationUuid(String iterationUuid) {
        this.iterationUuid = iterationUuid;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
