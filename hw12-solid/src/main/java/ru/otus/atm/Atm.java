package ru.otus.atm;

import ru.otus.denomination.Denomination;
import ru.otus.exception.*;

public interface Atm {
    void depositMoney(Denomination denomination, int quantity) throws IncorrectBanknotesQuantityException;

    int withdrawMoney(int sum) throws WithdrawBanknotesCollectException, IncorrectSumWithdrawException;

    int getBalance();
}
