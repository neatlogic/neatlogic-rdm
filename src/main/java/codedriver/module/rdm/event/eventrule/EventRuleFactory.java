package codedriver.module.rdm.event.eventrule;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.module.rdm.dao.mapper.RdmEventMapper;
import codedriver.module.rdm.event.dto.EventRuleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventRuleFactory {

    @Resource
    private RdmEventMapper rdmEventMapper;

    private Map<String, Map<String, List<EventRuleVo>>> ruleMap = new ConcurrentHashMap<>();

    //动态添加事件规则
    public void addRule(EventRuleVo eventRuleVo) {
        String tenantUuid = TenantContext.get().getTenantUuid();
        String key = eventRuleVo.getObjectUuid() + "||" + eventRuleVo.getObjectBelong() + "||" + eventRuleVo.getEvent();
        if (ruleMap.containsKey(tenantUuid)) {
            Map<String, List<EventRuleVo>> tenantRuleMap = ruleMap.get(tenantUuid);
            if (tenantRuleMap.containsKey(key)) {
                ruleMap.get(tenantUuid).get(key).add(eventRuleVo);
            }
        } else {
            Map<String, List<EventRuleVo>> tenantRuleMap = new HashMap<>();
            ruleMap.put(tenantUuid, tenantRuleMap);
            ruleMap.get(tenantUuid).put(key, new ArrayList<EventRuleVo>() {{
                add(eventRuleVo);
            }});
        }
    }

    //根据事件匹配规则
    public List<EventRuleVo> findMatchRule(String objectUuid, String objectBelong, String eventName) {
        String key = objectUuid + "||" + objectBelong + "||" + eventName;
        String tenantUuid = TenantContext.get().getTenantUuid();
        if (ruleMap.containsKey(tenantUuid)) {
            Map<String, List<EventRuleVo>> tenantRuleMap = ruleMap.get(tenantUuid);
            if (tenantRuleMap.containsKey(key)) {
                return tenantRuleMap.get(key);
            }
        } else {
            Map<String, List<EventRuleVo>> tenantRuleMap = new HashMap<>();
            List<EventRuleVo> eventRuleList = rdmEventMapper.getAllEventRule();
            tenantRuleMap.put(key, eventRuleList);
            ruleMap.put(tenantUuid, tenantRuleMap);
            return eventRuleList;
        }
        return new ArrayList<>();
    }
}
