package codedriver.module.rdm.event.dto;

/**
 * @ClassName EventRuleVo
 * @Description
 * @Auther fandong
 * @Date 2020/1/7 14:45
 **/
public class EventRuleVo {
    private String uuid;
    private String objectUuid;
    private String objectBelong;
    private String event;

    private String triggerRule;

    private String targetObjectUuid;
    private String targetObjectBelong;
    private String completeRule;

    public EventRuleVo() {

    }

    public EventRuleVo(EventRuleVo eventRuleVo) {
        setObjectUuid(eventRuleVo.getObjectUuid());
        setObjectBelong(eventRuleVo.getObjectBelong());
        setEvent(eventRuleVo.getEvent());
        setTargetObjectUuid(eventRuleVo.getTargetObjectUuid());
        setTargetObjectBelong(eventRuleVo.getTargetObjectBelong());
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getObjectUuid() {
        return objectUuid;
    }

    public void setObjectUuid(String objectUuid) {
        this.objectUuid = objectUuid;
    }

    public String getObjectBelong() {
        return objectBelong;
    }

    public void setObjectBelong(String objectBelong) {
        this.objectBelong = objectBelong;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTriggerRule() {
        return triggerRule;
    }

    public void setTriggerRule(String triggerRule) {
        this.triggerRule = triggerRule;
    }

    public String getTargetObjectUuid() {
        return targetObjectUuid;
    }

    public void setTargetObjectUuid(String targetObjectUuid) {
        this.targetObjectUuid = targetObjectUuid;
    }

    public String getTargetObjectBelong() {
        return targetObjectBelong;
    }

    public void setTargetObjectBelong(String targetObjectBelong) {
        this.targetObjectBelong = targetObjectBelong;
    }

    public String getCompleteRule() {
        return completeRule;
    }

    public void setCompleteRule(String completeRule) {
        this.completeRule = completeRule;
    }
}
