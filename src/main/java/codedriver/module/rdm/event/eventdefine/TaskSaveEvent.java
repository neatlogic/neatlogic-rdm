package codedriver.module.rdm.event.eventdefine;

import codedriver.module.rdm.event.core.EventTemplate;
import codedriver.module.rdm.event.objectbelong.ProcessAreaBelong;

/**
 * @ClassName TaskSaveEvent
 * @Description
 * @Auther
 * @Date 2020/1/7 11:53
 **/

public class TaskSaveEvent extends EventTemplate {

    public TaskSaveEvent(String _uniqueKey, String _objectUuid, String _belong) {
        super(_uniqueKey, _objectUuid, _belong);
    }

    public TaskSaveEvent(String _uniqueKey, ProcessAreaBelong _processAreaBelong) {
        super(_uniqueKey, _processAreaBelong);
    }

    @Override
    public String getName() {
        return "task_save";
    }

    @Override
    public String getDescription() {
        return "任务保存";
    }

}
