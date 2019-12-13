package codedriver.module.rdm.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

import java.util.List;

/**
 * @ClassName ProcessAreaVo
 * @Description 过程域类
 * @Auther
 * @Date 2019/12/4 15:56
 **/
public class ProcessAreaVo extends BasePageVo {

    /**
     * 自增主键
     */
    @EntityField(name = "自增主键",type = ApiParamType.STRING)
    private Long id;

    /**
     * 过程域查询关键字
     */
    private transient String keyword;

    /**
     * 过程域名称
     */
    @EntityField(name = "过程域名称",type = ApiParamType.STRING)
    private String name;

    /**
     * 过程域描述
     */
    private String description;

    /**
     * 过程域uuid
     */
    @EntityField(name = "过程域uuid",type = ApiParamType.STRING)
    private String uuid;

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

    /**
     * 过程域属性
     */
    private List<FieldVo> fieldList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public List<FieldVo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldVo> fieldList) {
        this.fieldList = fieldList;
    }
}
