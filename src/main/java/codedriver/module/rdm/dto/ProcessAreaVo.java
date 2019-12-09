package codedriver.module.rdm.dto;

import codedriver.framework.common.dto.BasePageVo;

/**
 * @ClassName ProcessAreaVo
 * @Description 过程域类
 * @Auther r2d2
 * @Date 2019/12/4 15:56
 **/
public class ProcessAreaVo extends BasePageVo {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 过程域名称
     */
    private String name;

    /**
     * 过程域uuid
     */
    private String uuid;

    /**
     * 过程域查询关键字
     */
    private transient String keyword;

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
}
