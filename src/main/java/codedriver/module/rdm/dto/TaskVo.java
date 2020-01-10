package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.dto.UserVo;
import codedriver.framework.restful.annotation.EntityField;

import java.util.List;

/**
 * @ClassName TaskVo
 * @Description 任务类
 * @Auther
 * @Date 2019/12/4 18:17
 **/
public class TaskVo {

    /**
     * 处理人集合
     */
    public List<UserVo> processAccountList;
    private Long id;
    @EntityField(name = "任务uuid", type = ApiParamType.STRING)
    private String uuid;
    @EntityField(name = "任务名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "任务描述", type = ApiParamType.STRING)
    private String description;
    @EntityField(name = "项目uuid", type = ApiParamType.STRING)
    private String projectUuid;
    @EntityField(name = "项目名称", type = ApiParamType.STRING)
    private String projectName;
    @EntityField(name = "迭代id", type = ApiParamType.STRING)
    private String iterationUuid;
    @EntityField(name = "迭代名称", type = ApiParamType.STRING)
    private String iterationName;
    @EntityField(name = "迭代名称", type = ApiParamType.STRING)
    private String processAreaUuid;
    @EntityField(name = "过程域名称", type = ApiParamType.STRING)
    private String processAreaName;
    @EntityField(name = "父任务Uuid", type = ApiParamType.STRING)
    private String parentUuid;
    @EntityField(name = "状态uuid", type = ApiParamType.STRING)
    private String statusUuid;
    @EntityField(name = "状态名称", type = ApiParamType.STRING)
    private String statusName;
    @EntityField(name = "优先级uuid", type = ApiParamType.STRING)
    private String priorityUuid;
    @EntityField(name = "优先级名称", type = ApiParamType.STRING)
    private String priorityName;
    @EntityField(name = "优先级颜色", type = ApiParamType.STRING)
    private String priorityColor;
    @EntityField(name = "类别Uuid", type = ApiParamType.STRING)
    private String categoryUuid;
    @EntityField(name = "类别名称", type = ApiParamType.STRING)
    private String categoryName;
    @EntityField(name = "处理人id集合", type = ApiParamType.JSONARRAY)
    private List<String> processAccountIdList;
    @EntityField(name = "开始时间", type = ApiParamType.STRING)
    private String startTime;

    @EntityField(name = "结束时间", type = ApiParamType.STRING)
    private String endTime;

    @EntityField(name = "创建人", type = ApiParamType.STRING)
    private String createUser;

    @EntityField(name = "创建时间", type = ApiParamType.STRING)
    private String createTime;

    @EntityField(name = "修改人", type = ApiParamType.STRING)
    private String updateUser;

    @EntityField(name = "修改时间", type = ApiParamType.STRING)
    private String updateTime;

    /**
     * 该值是一个json数组，为将来数据库扩展准备()
     */
    private String customFields;

    @EntityField(name = "任务属性集合", type = ApiParamType.JSONARRAY)
    private List<FieldVo> taskFieldList;

    @EntityField(name = "任务附件集合", type = ApiParamType.JSONARRAY)
    private List<TaskFileVo> taskFileVoList;

    private List<TaskAssociateVo> associatedTaskList;

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

    public List<TaskFileVo> getTaskFileVoList() {
        return taskFileVoList;
    }

    public void setTaskFileVoList(List<TaskFileVo> taskFileVoList) {
        this.taskFileVoList = taskFileVoList;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
    }

    public List<TaskAssociateVo> getAssociatedTaskList() {
        return associatedTaskList;
    }

    public void setAssociatedTaskList(List<TaskAssociateVo> associatedTaskList) {
        this.associatedTaskList = associatedTaskList;
    }
}
