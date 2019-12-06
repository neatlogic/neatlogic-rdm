package codedriver.module.rdm.core;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiRequestType {
    String type() default "GET";
}
