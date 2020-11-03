package com.example.flutterinnative.methodinject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: MyAnnotationDefinition
 * @Author: dongke
 * @Date: 2020/11/3 17:01
 * @Description:
 */

@Target(value = {ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MyAnnotationDefinition {
    /*定义注解里面的参数信息*/
    String name();

    String value();

    String path();
}
