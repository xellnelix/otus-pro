package ru.otus.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

@SuppressWarnings({"java:S3011", "java:S112", "java:S106", "java:S2696"})
public class TestStarter {
    private static int testPassed = 0;
    private static int testFailed = 0;

    public void startTest(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }

        for (Method method : testMethods) {
            Object testClassInstance = ReflectionHelper.instantiate(clazz);
            ReflectionHelper.callMethod(testClassInstance, beforeMethods.get(0).getName());
            try {
                ReflectionHelper.callMethod(testClassInstance, method.getName());
                testPassed += 1;
            } catch (Exception ex) {
                testFailed += 1;
            }
            ReflectionHelper.callMethod(testClassInstance, afterMethods.get(0).getName());
        }

        System.out.println("Tests passed: " + testPassed);
        System.out.println("Tests failed: " + testFailed);
    }
}
