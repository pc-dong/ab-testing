package cn.dpc.abtesting.api.rsocket.config.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostMessageMapping {

    String[] value() default {};
}
