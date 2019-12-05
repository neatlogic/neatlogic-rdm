package codedriver.module.rdm.dto;

/**
 * @ClassName ProcessAreaVo
 * @Description 过程域类
 * @Auther r2d2
 * @Date 2019/12/4 15:56
 **/
public class ProcessAreaVo {
    private Long id;
    private String name;
    private String uuid;

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
}
