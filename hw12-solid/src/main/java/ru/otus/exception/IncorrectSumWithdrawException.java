package ru.otus.exception;

public class IncorrectSumWithdrawException extends Exception {
    public IncorrectSumWithdrawException(String errorMessage) {
        super(errorMessage);
    }
}
