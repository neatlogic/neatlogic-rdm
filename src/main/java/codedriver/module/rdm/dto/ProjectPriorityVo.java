package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @ClassName ProjectPriorityVo
 * @Description 优先级类
 * @Auther
 * @Date 2019/12/4 18:23
 **/
public class ProjectPriorityVo extends BasePageVo {

    /**
     * 自增主键
     */
    @EntityField(name = "自增主键",type = ApiParamType.LONG)
    private Long id;

    /**
     * 优先级查询关键字
     */
    private transient String keyword;

    /**
     * 优先级uuid
     */
    @EntityField(name = "优先级uuid",type = ApiParamType.STRING)
    private String uuid;

    /**
     * 优先级名称
     */
    @EntityField(name = "优先级名称",type = ApiParamType.STRING)
    private String name;

    /**
     * 优先级颜色
     */
    @EntityField(name = "优先级颜色",type = ApiParamType.STRING)
    private String color;

    /**
     * 过程域uuid
     */
    private String processAreaUuid;

    /**
     * 项目uuid
     */
    private String projectUuid;

    /**
     * 创建人
     */
    @EntityField(name = "创建人",type = ApiParamType.STRING)
    private String createUser;

    /**
     * 创建时间
     */
    @EntityField(name = "创建时间",type = ApiParamType.STRING)
    private String createTime;

    /**
     * 修改人
     */
    @EntityField(name = "修改人",type = ApiParamType.STRING)
    private String updateUser;

    /**
     * 修改时间
     */
    @EntityField(name = "修改时间",type = ApiParamType.STRING)
    private String updateTime;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
