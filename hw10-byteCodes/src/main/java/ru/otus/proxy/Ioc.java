package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.Log;

@SuppressWarnings("unchecked")
public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    public static <T> T createTestLogging(T classObj) {
        Class<?> clazz = classObj.getClass();
        InvocationHandler handler = new DemoInvocationHandler<>(classObj);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(), clazz.getInterfaces(), handler);
    }

    static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T classObj;
        private final List<Method> logAnnotationMethods = new ArrayList<>();

        DemoInvocationHandler(T classObj) {
            Method[] declaredMethods = classObj.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Log.class)) {
                    logAnnotationMethods.add(method);
                }
            }
            this.classObj = classObj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method targetMethod = classObj.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (logAnnotationMethods.contains(targetMethod)) {
                for (Object arg : args) {
                    logger.info("invoking method with args:{}", arg);
                }
            }
            return method.invoke(classObj, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + classObj + '}';
        }
    }
}
