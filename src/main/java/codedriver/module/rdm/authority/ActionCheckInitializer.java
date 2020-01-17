package codedriver.module.rdm.authority;

import codedriver.module.rdm.annotation.ActionCheck;
import codedriver.module.rdm.annotation.InputParam;
import codedriver.module.rdm.dto.ActionCheckVo;
import codedriver.module.rdm.exception.role.ActionCheckParamErrorException;
import codedriver.module.rdm.exception.role.ActionCheckValueConflictException;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-13 19:03
 **/
public class ActionCheckInitializer implements WebApplicationInitializer {

    private static Map<String, String> roleActionMap = new HashMap<>();

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        Reflections reflections = new Reflections("codedriver", new MethodAnnotationsScanner());
        Set<Method> methods =  reflections.getMethodsAnnotatedWith(ActionCheck.class);
        for (Method method : methods){
            ActionCheck check = AnnotationUtils.findAnnotation(method, ActionCheck.class);
            if (check != null){
                boolean valid = false;
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                if (parameterAnnotations.length == 1 && parameterAnnotations[0].length == 1){
                    if (parameterAnnotations[0][0] instanceof InputParam){
                        AnnotatedType[] annotatedTypes =  method.getAnnotatedParameterTypes();
                        AnnotatedType annotatedType = annotatedTypes[0];
                        if(annotatedType.getType().getTypeName().equals(ActionCheckVo.class.getName())){
                            valid = true;
                        }
                    }
                }
                if (valid){
                    String key = check.value();
                    String value = check.name();
                    if (roleActionMap.containsKey(key) && !roleActionMap.get(key).equals(value)){
                        throw new ActionCheckValueConflictException();
                    }else {
                        roleActionMap.put(key, value);
                    }
                }else {
                    throw new ActionCheckParamErrorException();
                }
            }
        }
    }
}
