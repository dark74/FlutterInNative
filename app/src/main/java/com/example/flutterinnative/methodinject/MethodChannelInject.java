package com.example.flutterinnative.methodinject;


/**
 * @ClassName: MethodChannelInject
 * @Author: dongke
 * @Date: 2020/11/3 19:58
 * @Description: 1.注册监听flutter对原生的调用 2.使用MethodChannel.invoke flutter中的方法
 */
public @interface MethodChannelInject {
    String methodName();
    String [] paramsKey();
    String result() default "";
}
