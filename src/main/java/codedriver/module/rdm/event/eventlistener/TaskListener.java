package codedriver.module.rdm.event.eventlistener;

import codedriver.module.rdm.event.core.ListenerTemplate;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TaskListener
 * @Description
 * @Auther
 * @Date 2020/1/8 15:14
 **/
@Service
public class TaskListener extends ListenerTemplate {

    @Override
    public String getName() {
        return "task_listener";
    }

    @Override
    public List<String> getEventList() {
        return new ArrayList<String>() {{
            add("task_save");
        }};
    }

    @Override
    protected JSONObject getCompleteParam(String uniqueKey, String objectUuid, String objectBelong, String eventName) {
        return null;
    }
}
