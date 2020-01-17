package codedriver.module.rdm.event.dto;

import codedriver.module.rdm.event.core.Belong;

/**
 * @ClassName RelationVo
 * @Description
 * @Auther
 * @Date 2020/1/8 17:32
 **/
public class RelationVo {

    private Belong objectBelong;

    private Belong targteObjectBelong;

    public RelationVo(Belong objectBelong, Belong targteObjectBelong) {
        this.objectBelong = objectBelong;
        this.targteObjectBelong = targteObjectBelong;
    }


    public Belong getObjectBelong() {
        return objectBelong;
    }

    public void setObjectBelong(Belong objectBelong) {
        this.objectBelong = objectBelong;
    }

    public Belong getTargteObjectBelong() {
        return targteObjectBelong;
    }

    public void setTargteObjectBelong(Belong targteObjectBelong) {
        this.targteObjectBelong = targteObjectBelong;
    }
}
