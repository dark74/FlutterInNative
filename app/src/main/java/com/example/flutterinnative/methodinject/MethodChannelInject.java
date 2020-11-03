package com.example.flutterinnative.methodinject;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @ClassName: MethodChannelInject
 * @Author: dongke
 * @Date: 2020/11/3 19:58
 * @Description: 1.注册监听flutter对原生的调用 2.使用MethodChannel.invoke flutter中的方法
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MethodChannelInject {
    String methodName();

    String[] paramsKey();

    String result() default "";
}
