package codedriver.module.rdm.dto;

import java.util.List;

/**
 * @ClassName ProjectVo
 * @Description 项目类
 * @Auther fandong
 * @Date 2019/12/4 15:55
 **/
public class ProjectVo {
    private Long id;
    private String name;
    private String uuid;
    private List<FieldVo> fieldList;

    private List<IterationVo> iterationList;

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

    public List<FieldVo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldVo> fieldList) {
        this.fieldList = fieldList;
    }

    public List<IterationVo> getIterationList() {
        return iterationList;
    }

    public void setIterationList(List<IterationVo> iterationList) {
        this.iterationList = iterationList;
    }
}
