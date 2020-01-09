package codedriver.module.rdm.event.core;

import codedriver.framework.asynchronization.thread.CodeDriverThread;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @ClassName RdmEventTriggerThread
 * @Description
 * @Auther
 * @Date 2020/1/8 14:50
 **/
public abstract class EventTriggerThread extends CodeDriverThread {

    private Event event;

    public EventTriggerThread(Event event) {
        this.event = event;
    }

}
