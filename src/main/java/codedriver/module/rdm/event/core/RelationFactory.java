package codedriver.module.rdm.event.core;

import codedriver.framework.common.RootComponent;
import codedriver.module.rdm.event.dto.RelationVo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

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
public class RelationFactory implements ApplicationListener<ContextRefreshedEvent> {

    private static Map<String, Relation> relationMap = new ConcurrentHashMap<>();

    public static Relation getRelation(String objectBelong, String targetObjectBelong) {
        String key = objectBelong + "||" + targetObjectBelong;
        return relationMap.get(key);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        Map<String, Relation> myMap = context.getBeansOfType(Relation.class);
        for (Map.Entry<String, Relation> entry : myMap.entrySet()) {
            Relation relation = entry.getValue();
            List<RelationVo> relationVoList = relation.getRelationList();
            for (RelationVo relationVo : relationVoList) {
                String key = relationVo.getObjectBelong().name() + "||" + relationVo.getTargteObjectBelong().name();
                relationMap.put(key, relation);
            }
        }
    }


}
