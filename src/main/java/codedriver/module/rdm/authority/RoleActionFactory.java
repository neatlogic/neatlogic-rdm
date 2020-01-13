package codedriver.module.rdm.authority;

import codedriver.framework.common.RootComponent;
import codedriver.module.rdm.annotation.ActionCheck;
import codedriver.module.rdm.annotation.RoleAction;
import codedriver.module.rdm.dto.ActionCheckVo;
import codedriver.module.rdm.exception.role.ActionCheckParamErrorException;
import codedriver.module.rdm.exception.role.ActionCheckValueConflictException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-13 11:50
 **/
@RootComponent
public class RoleActionFactory implements ApplicationListener<ContextRefreshedEvent> {

    private static Map<String, String> roleActionMap = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, Object> map = context.getBeansWithAnnotation(RoleAction.class);
        for(Map.Entry<String, Object> entry : map.entrySet()){
            Object bean = entry.getValue();
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods){
                ActionCheck check = AnnotationUtils.findAnnotation(method, ActionCheck.class);
                if (check != null){
                    Class[] params = method.getParameterTypes();
                    if (params.length > 0 && (params[0] == ActionCheckVo.class)){
                        String key = check.value();
                        String value = check.name();
                        if (map.containsKey(key) && !map.get(key).equals(value)){
                            throw new ActionCheckValueConflictException();
                        }else {
                            roleActionMap.put(key, value);
                        }
                    }else {
                        throw new  ActionCheckParamErrorException();
                    }
                }
            }
        }
    }
}
