package codedriver.module.rdm.event.core;

import codedriver.framework.common.RootComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName EventListenerFactory
 * @Description
 * @Auther fandong
 * @Date 2020/1/8 15:22
 **/
@RootComponent
public class ListenerFactory implements ApplicationListener<ContextRefreshedEvent> {

    private static Map<String, Listener> listenerMap = new ConcurrentHashMap<>();

    private static Map<String, List<Listener>> eventMap = new ConcurrentHashMap<>();

    public static Listener getListenerByName(String listenerName) {
        return listenerMap.get(listenerName);
    }

    //一个事件可能被多个地方监听
    public static List<Listener> getListenerByEventName(String eventName) {
        return eventMap.get(eventName);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        Map<String, Listener> myMap = context.getBeansOfType(Listener.class);
        for (Map.Entry<String, Listener> entry : myMap.entrySet()) {
            Listener listener = entry.getValue();
            if (listener.getEventList() != null) {
                String listenerName = listener.getName();
                List<String> eventList = listener.getEventList();
                listenerMap.put(listenerName, listener);

                for (String event : eventList) {
                    if (eventMap.containsKey(event)) {
                        eventMap.get(event).add(listener);
                    } else {
                        eventMap.put(event, new ArrayList<Listener>() {{
                            add(listener);
                        }});
                    }
                    break;
                }
            }
        }
    }

}
