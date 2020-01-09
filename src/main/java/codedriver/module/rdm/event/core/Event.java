package codedriver.module.rdm.event.core;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName RdmEvent
 * @Description
 * @Auther
 * @Date 2020/1/7 16:33
 **/
public interface Event {

    String getName();

    String getDescription();

    String getUniqueKey();

    JSONObject getParam();

    String getObjectUuid();

    String getBelong();
}
