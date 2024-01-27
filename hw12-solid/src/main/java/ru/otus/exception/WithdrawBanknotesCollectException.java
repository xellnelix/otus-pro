package ru.otus.exception;

public class WithdrawBanknotesCollectException extends Exception {
    public WithdrawBanknotesCollectException(String errorMessage) {
        super(errorMessage);
    }
}
