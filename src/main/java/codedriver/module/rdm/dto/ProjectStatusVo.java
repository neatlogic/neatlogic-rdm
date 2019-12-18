package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

import java.util.List;

/**
 * @ClassName StatusVo
 * @Description 项目工作流状态
 * @Auther
 * @Date 2019/12/4 18:23
 **/
public class ProjectStatusVo extends BasePageVo {

    /**
     * 自增主键
     */
    @EntityField(name = "自增主键",type = ApiParamType.LONG)
    private Long id;

    /**
     * 状态uuid
     */
    @EntityField(name = "状态uuid",type = ApiParamType.STRING)
    private String uuid;

    /**
     * 状态名称
     */
    @EntityField(name = "状态名称",type = ApiParamType.STRING)
    private String name;

    /**
     * 项目uuid
     */
    @EntityField(name = "项目uuid",type = ApiParamType.STRING)
    private String projectUuid;

    /**
     * 过程域uuid
     */
    @EntityField(name = "过程域uuid",type = ApiParamType.STRING)
    private String processAreaUuid;

    /**
     * 类型
     */
    @EntityField(name = "类型",type = ApiParamType.STRING)
    private String type;

    /**
     * 状态转移列表
     */
    @EntityField(name = "状态转移列表",type = ApiParamType.JSONARRAY)
    public List<String> transferTo;


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

    public List<String> getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(List<String> transferTo) {
        this.transferTo = transferTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
