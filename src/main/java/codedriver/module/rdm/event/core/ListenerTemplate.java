package codedriver.module.rdm.event.core;

import codedriver.module.rdm.dao.mapper.RdmEventMapper;
import codedriver.module.rdm.event.dto.EventRuleVo;
import codedriver.module.rdm.event.dto.EventVo;
import codedriver.module.rdm.event.eventrule.EventRuleFactory;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName ListenerTemplate
 * @Description
 * @Auther
 * @Date 2020/1/8 15:13
 **/
public abstract class ListenerTemplate implements Listener {

    @Resource
    private EventRuleFactory eventRuleFactory;

    @Resource
    private RdmEventMapper rdmEventMapper;

    @Override
    public List<Long> triggerEvent(String uniqueKey, JSONObject triggerParam, String objectUuid, String objectBelong, String eventName) {
        List<Long> eventIdList = new ArrayList<>();
        List<EventRuleVo> ruleList = eventRuleFactory.findMatchRule(objectUuid, objectBelong, eventName);
        for (EventRuleVo rule : ruleList) {
            EventVo event = new EventVo(rule);
            event.setUniqueKey(uniqueKey);

            //TODO 人员信息将来由表达式获取  如： #{createUser} #{}
            event.setUserId("fandong");
            String triggerRule = rule.getTriggerRule();

            // 校验触发规则 , 没有触发规则则事件触发即默认生成任务
            boolean needTrigger = true;
            if(StringUtils.isNotBlank(triggerRule)){
                needTrigger = runScript(triggerParam, triggerRule);
            }


            //检验通过检查事件是否存在，不存在就创建 , 测试一直为true
            needTrigger =  true;
            if (needTrigger) {
                EventVo eventResultVo = rdmEventMapper.getEvent(event);
                if (eventResultVo == null) {
                    rdmEventMapper.insertEvent(event);
                    eventIdList.add(event.getId());
                }
            }
        }
        return eventIdList;
    }

    protected abstract JSONObject getCompleteParam(String uniqueKey, String objectUuid, String objectBelong, String eventName);

    @Override
    public void completeEvent(String uniqueKey, String objectUuid, String objectBelong, String eventName) {

//        JSONObject param = getCompleteParam(uniqueKey, objectUuid, objectBelong, eventName);


        EventVo event = rdmEventMapper.getEvent(new EventVo(uniqueKey, objectUuid, objectBelong, eventName));

        String ruleUuid = event.getRuleUuid();

        EventRuleVo eventRuleVo = rdmEventMapper.getEventRuleByUuid(ruleUuid);
        String completeRule = eventRuleVo.getCompleteRule();

        String targetObjectUuid = eventRuleVo.getTargetObjectUuid();
        String targetObjectBelong = eventRuleVo.getTargetObjectBelong();
        JSONObject completeParam = matchRelationAndGetCheckParam(uniqueKey, objectBelong, objectUuid, targetObjectBelong, targetObjectUuid );

        boolean complete;
        if (StringUtils.isBlank(completeRule)) {
            //如果没有关闭规则，则默认 有关联即关闭
            complete = completeParam.getIntValue("relationCount") > 0 ;
        } else {
            complete = runScript(completeParam, completeRule);
        }

        if (complete) {
            rdmEventMapper.deleteEventById(event.getId());
        }
    }

    private JSONObject matchRelationAndGetCheckParam(String uniqueKey, String objectBelong, String objectUuid, String targetObjectBelong, String targetObjectUuid){
        JSONObject result = new JSONObject();
        String key = objectBelong + "||" + targetObjectBelong;
        Relation relation = RelationFactory.getRelation(objectBelong, targetObjectBelong);
        Integer relationCount = relation.countRelation(key, uniqueKey, objectUuid, targetObjectUuid);
        JSONObject param = relation.getRelationParam(key, uniqueKey, objectUuid, targetObjectUuid);
        result.put("relationCount", relationCount);
        result.put("param", param);
        return result;
    }


    public boolean runScript(JSONObject param, String rule) {
        boolean result = true;
        try {
            if (!StringUtils.isBlank(rule)) {
                Pattern pattern = null;
                Matcher matcher = null;
                StringBuffer temp = new StringBuffer();
                String regex = "\\$\\{([^}]+)\\}";
                pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(rule);
                List<String> stepAndKeyList = new ArrayList<String>();
                while (matcher.find()) {
                    matcher.appendReplacement(temp, "map[\"" + matcher.group(1) + "\"]");
                    stepAndKeyList.add(matcher.group(1));
                }
                matcher.appendTail(temp);

                StringBuilder script = new StringBuilder();
                script.append("function run(){");
                script.append("var map = new Object();");

                if (stepAndKeyList.size() > 0) {
                    for (String stepAndKey : stepAndKeyList) {
                        if (stepAndKey.indexOf(".") > -1 && stepAndKey.split("\\.").length == 2) {
                            String stepUid = stepAndKey.split("\\.")[0];
                            String key = stepAndKey.split("\\.")[1];
                            List<String> valueList = new ArrayList<>();

                            //TODO parse value list


                            if (valueList.size() > 0) {
                                if (valueList.size() > 1) {
                                    script.append("map[\"" + stepUid + "." + key + "\"] = [");
                                    String v = "[";
                                    for (int i = 0; i < valueList.size(); i++) {
                                        String value = valueList.get(i);
                                        script.append("\"" + value + "\"");
                                        v += "\"" + value + "\"";
                                        if (i < valueList.size() - 1) {
                                            script.append(",");
                                            v += ",";
                                        }
                                    }
                                    v += "]";
                                    script.append("];");
                                } else {
                                    script.append("map[\"" + stepUid + "." + key + "\"] = \"" + valueList.get(0) + "\";");
                                }
                            }
                        }
                    }
                }
                script.append("return " + temp.toString() + ";");
                script.append("}");
                ScriptEngineManager sem = new ScriptEngineManager();
                ScriptEngine se = sem.getEngineByName("nashorn");
                se.eval(script.toString());
                Invocable invocableEngine = (Invocable) se;
                Object callbackvalue = invocableEngine.invokeFunction("run");
                result = Boolean.parseBoolean(callbackvalue.toString());
            }
        } catch (NoSuchMethodException e) {
            result = false;
        } catch (ScriptException e) {
            result = false;
        }
        return result;
    }


}
