package ru.otus;

import ru.otus.reflection.TestStarter;

@SuppressWarnings({"java:S3011", "java:S112"})
public class Main {
    public static void main(String[] args) {
        TestStarter testStarter = new TestStarter();
        try {
            testStarter.startTest("ru.otus.reflection.TestClass");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
