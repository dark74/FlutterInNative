package com.example.flutterinnative.methodinject;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.flutter.Log;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * @ClassName: MethodChannelUtil
 * @Author: dongke
 * @Date: 2020/11/3 19:56
 * @Description:
 */
public class MethodChannelUtil {

    /**
     * 每次flutter调用原生时候，都遍历所有的方法，找出匹配的方法去执行
     *
     * @param clazz      activity或者fragment
     * @param methodCall flutter调用传递来的方法名和参数
     * @param result     返回给flutter的回调
     */
    public static void proxy(Object clazz, MethodCall methodCall, MethodChannel.Result result) {
        //获取flutter要调用的方法名
        String method = methodCall.method;
        Log.d("dk", "proxy - method:" + method);
        //获取activity或者fragment声明的所有公共方法
        Method[] methods = clazz.getClass().getDeclaredMethods();
        //遍历所有方法找出添加了注解的方法
        for (Method m : methods) {
            if (m.isAnnotationPresent(MethodChannelInject.class)) {
                MethodChannelInject annotation = m.getAnnotation(MethodChannelInject.class);
                //如果匹配到本次flutter要调用的方法
                if (annotation.methodName().equals(method)) {
                    Map<String, Object> map = new HashMap<>();
                    //按约束好的key名称，取出参数
                    for (String param : annotation.paramsKey()) {
                        Object p = methodCall.argument(param);
                        map.put(param, p);
                    }
                    try {
                        //使用反射调用方法进行执行
                        Object r = m.invoke(clazz, map);
                        if (r != null) {
                            //如果方法有返回值，则调用result的回调，执行flutter中的方法
                            result.success(r);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
