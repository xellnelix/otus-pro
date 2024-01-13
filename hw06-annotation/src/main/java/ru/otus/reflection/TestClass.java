package ru.otus.reflection;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

@SuppressWarnings({"java:S3011", "java:S112", "java:S106"})
public class TestClass {
    @Before
    private void beforeMethod() {
        System.out.println("Run before method");
    }

    @Test
    private void firstTestMethod() {
        System.out.println("Run first test method");
        throw new RuntimeException();
    }

    @Test
    public void secondTestMethod() {
        System.out.println("Run second test method");
    }

    @After
    private void afterMethod() {
        System.out.println("Run after method");
    }
}
