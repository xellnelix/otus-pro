package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.Log;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    public static TestLoggingInterface createTestLogging() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final List<Method> logAnnotationMethods = new ArrayList<>();

        DemoInvocationHandler(TestLoggingInterface myClass) {
            Method[] declaredMethods = myClass.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Log.class)) {
                    logAnnotationMethods.add(method);
                }
            }
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method targetMethod = myClass.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (logAnnotationMethods.contains(targetMethod)) {
                for (Object arg : args) {
                    logger.info("invoking method with args:{}", arg);
                }
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
