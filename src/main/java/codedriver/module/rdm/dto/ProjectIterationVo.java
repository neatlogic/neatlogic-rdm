package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @ClassName Itearator
 * @Description 迭代类
 * @Auther
 * @Date 2019/12/4 18:10
 **/
public class ProjectIterationVo extends BasePageVo {

    /**
     * 自增主键
     */
    @EntityField(name = "自增主键", type = ApiParamType.LONG)
    private Long id;

    /**
     * 迭代查询关键字
     */
    private transient String keyword;

    /**
     * 项目uuid
     */
    @EntityField(name = "项目uuid", type = ApiParamType.STRING)
    private String projectUuid;

    /**
     * 项目迭代uuid
     */
    @EntityField(name = "项目迭代uuid", type = ApiParamType.STRING)
    private String uuid;

    /**
     * 迭代名称
     */
    @EntityField(name = "迭代名称", type = ApiParamType.STRING)
    private String name;

    /**
     * 迭代开始时间
     */
    @EntityField(name = "迭代开始时间", type = ApiParamType.STRING)
    private String startDate;

    /**
     * 迭代结束时间
     */
    @EntityField(name = "迭代结束时间", type = ApiParamType.STRING)
    private String endDate;

    /**
     * 迭代描述
     */
    private String description;


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

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
