package codedriver.module.rdm.event.core;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName EventTemplate
 * @Description
 * @Auther
 * @Date 2020/1/7 16:33
 **/
public abstract class EventTemplate implements Event {

    private String uniqueKey;
    private JSONObject param;
    private String belong;
    private String objectUuid;

    public EventTemplate(String _uniqueKey, JSONObject _param, String _objectUuid, String _belong){
        this.uniqueKey = _uniqueKey;
        this.param = _param;
        this.belong = _belong;
        this.objectUuid = _objectUuid;
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public JSONObject getParam() {
        return param;
    }

    @Override
    public String getBelong() {
        return belong;
    }

    @Override
    public String getObjectUuid() {
        return objectUuid;
    }
}
