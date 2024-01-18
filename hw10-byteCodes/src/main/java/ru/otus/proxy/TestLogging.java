package ru.otus.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.Log;

public class TestLogging implements TestLoggingInterface {
    private static final Logger logger = LoggerFactory.getLogger(TestLogging.class);

    @Override
    @Log
    public void calculation(int param) {
        logger.info("Calculation with 1 argument");
    }

    @Override
    @Log
    public void calculation(int param, int secondParam) {
        logger.info("Calculation with 2 arguments");
    }

    @Override
    public void wrongCalculation(int param, int secondParam, int thirdParam) {
        logger.info("Calculation without log");
    }
}
