package codedriver.module.rdm.event.core;

import java.util.List;

/**
 * @ClassName EventListener
 * @Description
 * @Auther
 * @Date 2020/1/8 15:12
 **/
public interface Listener {

    default String getClassId() {
        return this.getClassId();
    }

    String getName();

    //可以监听哪些事件
    List<String> getEventList();

    //触发事件
    List<Long> triggerEvent(String uniqueKey, String objectUuid, String objectBelong, String eventName);

    //返回事件完成
    void completeEvent(String uniqueKey, String objectUuid, String objectBelong, String eventName);

}
