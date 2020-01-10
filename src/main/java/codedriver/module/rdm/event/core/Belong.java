package codedriver.module.rdm.event.core;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @ClassName ObjectBelong
 * @Description
 * @Auther
 * @Date 2020/1/7 12:18
 **/
public interface Belong {

    default String getClassName() {
        return this.getClass().getName();
    }

    String name();

    String description();

    String getBelongUuid();

    List<JSONObject> getBelongObjects();

}
