package ru.otus.exception;

public class IncorrectBanknotesQuantityException extends Exception {
    public IncorrectBanknotesQuantityException(String errorMessage) {
        super(errorMessage);
    }
}
