package codedriver.module.rdm.event.core;

/**
 * @ClassName EventTemplate
 * @Description
 * @Auther
 * @Date 2020/1/7 16:33
 **/
public abstract class EventTemplate implements Event {

    private String uniqueKey;
    private String belong;
    private String objectUuid;

    public EventTemplate(String _uniqueKey, String _objectUuid, String _belong) {
        this.uniqueKey = _uniqueKey;
        this.belong = _belong;
        this.objectUuid = _objectUuid;
    }

    public EventTemplate(String _uniqueKey, Belong _belong) {
        this.uniqueKey = _uniqueKey;
        this.belong = _belong.name();
        this.objectUuid = _belong.getBelongUuid();
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
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
