package codedriver.module.rdm.dto;

/**
 * @ClassName Itearator
 * @Description 迭代类
 * @Auther r2d2
 * @Date 2019/12/4 18:10
 **/
public class IterationVo {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 项目迭代uuid
     */
    private String uuid;

    /**
     * 迭代名称
     */
    private String name;

    /**
     * 迭代开始时间
     */
    private String startTime;

    /**
     * 迭代结束时间
     */
    private String endTime;

    /**
     * 迭代描述
     */
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
