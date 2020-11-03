package com.example.flutterinnative.methodinject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ClassName: MyAnnotationTest
 * @Author: dongke
 * @Date: 2020/11/3 18:03
 * @Description:
 */
public class MyAnnotationTest {
    public static void main(String[] args) throws Exception {
        Class clazz = Class.forName("com.example.flutterinnative.methodinject.TestMyAnnotation");

        //获取类注解
        MyAnnotationDefinition clazzAnnotation = (MyAnnotationDefinition) clazz.getAnnotation(MyAnnotationDefinition.class);
        System.out.println("类上注解- 注解名：" + clazzAnnotation.name() + "，注解值：" + clazzAnnotation.value() + ",注解路径：" + clazzAnnotation.path());
        //获取方法上注解
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            if (declaredMethods[i].isAnnotationPresent(MyAnnotationDefinition.class)) {
                MyAnnotationDefinition methodAnnotation = declaredMethods[i].getAnnotation(MyAnnotationDefinition.class);
                System.out.println("方法注解- 注解名：" + methodAnnotation.name() + "，注解值：" + methodAnnotation.value() + ",注解路径：" + methodAnnotation.path());
            }
        }
        //获取指定方法上的注解
        Method testAnnoMethod = clazz.getDeclaredMethod("testAnno");
        if (testAnnoMethod.isAnnotationPresent(MyAnnotationDefinition.class)) {
            MyAnnotationDefinition targetMethodAnnotation = testAnnoMethod.getAnnotation(MyAnnotationDefinition.class);
            System.out.println("指定方法注解- 注解名：" + targetMethodAnnotation.name() + "，注解值：" + targetMethodAnnotation.value() + ",注解路径：" + targetMethodAnnotation.path());
        }
        //获取属性上注解
        Field[] declaredFields = clazz.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            if (declaredFields[i].isAnnotationPresent(MyAnnotationDefinition.class)) {
                MyAnnotationDefinition filedAnnotation = declaredFields[i].getAnnotation(MyAnnotationDefinition.class);
                System.out.println("属性注解- 注解名：" + filedAnnotation.name() + "，注解值：" + filedAnnotation.value() + ",注解路径：" + filedAnnotation.path());
            }
        }
    }
}
