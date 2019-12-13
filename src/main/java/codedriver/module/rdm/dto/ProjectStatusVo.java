package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

import java.util.List;

/**
 * @ClassName StatusVo
 * @Description 项目工作流状态
 * @Auther
 * @Date 2019/12/4 18:23
 **/
public class ProjectStatusVo {

    /**
     * 自增主键
     */
    @EntityField(name = "自增主键",type = ApiParamType.STRING)
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
}
