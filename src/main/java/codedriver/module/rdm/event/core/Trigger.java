package codedriver.module.rdm.event.core;

import codedriver.framework.asynchronization.threadpool.CommonThreadPool;

import java.util.List;

/**
 * @ClassName RdmEventHandler
 * @Description
 * @Auther
 * @Date 2020/1/8 14:44
 **/
public class Trigger {

    public static void trigger(Event event) {

        CommonThreadPool.execute(new EventTriggerThread(event) {
            @Override
            protected void execute() {
                String eventName = event.getName();
                String uniqueKey = event.getUniqueKey();
                String objectUuid = event.getObjectUuid();
                String objectBelong = event.getBelong();
                List<Listener> listenerList = ListenerFactory.getListenerByEventName(event.getName());
                for (Listener listener : listenerList) {
                    //触发并生成事件
                    listener.triggerEvent(uniqueKey, objectUuid, objectBelong, eventName);

                    //校验事件是否完成
                    listener.completeEvent(uniqueKey, objectUuid, objectBelong, eventName);
                }
            }
        });
    }


    /*
     * 供外部调用的完成方法
     */
    private static void complete() {

    }

}
