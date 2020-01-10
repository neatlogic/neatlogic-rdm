package codedriver.module.rdm.event.eventlistener;

import codedriver.module.rdm.dao.mapper.TaskMapper;
import codedriver.module.rdm.dto.TaskVo;
import codedriver.module.rdm.event.core.ListenerTemplate;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private TaskMapper taskMapper;

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
    protected JSONObject getTriggerParam(String uniqueKey) {
        TaskVo taskVo = taskMapper.getTaskByUuid(uniqueKey);
        JSONObject param = JSONObject.parseObject(JSONObject.toJSONString(taskVo));
        return param;
    }

    @Override
    protected JSONObject getCompleteParam(String uniqueKey, String objectUuid, String objectBelong, String eventName) {
        return null;
    }
}
