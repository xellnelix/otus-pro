package ru.otus.proxy;

public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createTestLogging(new TestLogging());
        myClass.calculation(5);
        myClass.calculation(5, 6);
        myClass.wrongCalculation(5, 6, 7);
    }
}
