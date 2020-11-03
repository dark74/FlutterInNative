package com.example.flutterinnative.methodinject;

/**
 * @ClassName: TestMyAnnotation
 * @Author: dongke
 * @Date: 2020/11/3 17:03
 * @Description: 类、方法、属性上使用注解信息测试
 */

@MyAnnotationDefinition(name = "类名：", value = "TestMyAnnotation", path = "类路径")
public class TestMyAnnotation {
    @MyAnnotationDefinition(name = "属性名", value = "属性值", path = "属性路径")
    private String name;

    @MyAnnotationDefinition(name = "年龄", value = "18", path = "/user")
    private int age;

    @MyAnnotationDefinition(name = "方法名", value = "方法值", path = "方法路径")
    public String testAnno() {
        return "success!!!";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
