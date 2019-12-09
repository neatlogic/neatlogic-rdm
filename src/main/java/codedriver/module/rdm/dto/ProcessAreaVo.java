package codedriver.module.rdm.dto;

import codedriver.framework.common.dto.BasePageVo;

/**
 * @ClassName ProcessAreaVo
 * @Description 过程域类
 * @Auther
 * @Date 2019/12/4 15:56
 **/
public class ProcessAreaVo extends BasePageVo {
    private Long id;
    private String name;
    private String uuid;
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
