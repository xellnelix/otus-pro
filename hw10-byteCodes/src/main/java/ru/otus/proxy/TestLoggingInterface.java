package ru.otus.proxy;

public interface TestLoggingInterface {
    void calculation(int param);

    void calculation(int param, int secondParam);

    void wrongCalculation(int param, int secondParam, int thirdParam);
}
