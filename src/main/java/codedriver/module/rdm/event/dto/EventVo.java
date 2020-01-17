package codedriver.module.rdm.event.dto;

/**
 * @ClassName EventVo
 * @Description
 * @Auther
 * @Date 2020/1/7 18:21
 **/
public class EventVo {
    private Long id;
    private String uniqueKey;
    private String ruleUuid;
    private String objectUuid;
    private String objectBelong;
    private String event;
    private String triggerRule;
    private String targetObjectUuid;
    private String targetObjectBelong;
    private String completeRule;
    private String userId;
    private String status;


    public EventVo() {
    }

    public EventVo(EventRuleVo eventRuleVo) {
        this.ruleUuid = eventRuleVo.getUuid();
        this.objectUuid = eventRuleVo.getObjectUuid();
        this.objectBelong = eventRuleVo.getObjectBelong();
        this.triggerRule = eventRuleVo.getTriggerRule();
        this.targetObjectUuid = eventRuleVo.getTargetObjectUuid();
        this.targetObjectBelong = eventRuleVo.getTargetObjectBelong();
        this.completeRule = eventRuleVo.getCompleteRule();
        this.event = eventRuleVo.getEvent();
    }

    public EventVo(String _uniqueKey, String _objectUuid, String _objectBelong, String _eventName) {
        this.uniqueKey = _uniqueKey;
        this.objectUuid = _objectUuid;
        this.objectBelong = _objectBelong;
        this.event = _eventName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getRuleUuid() {
        return ruleUuid;
    }

    public void setRuleUuid(String ruleUuid) {
        this.ruleUuid = ruleUuid;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
