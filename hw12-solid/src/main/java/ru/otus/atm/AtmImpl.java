package ru.otus.atm;

import ru.otus.denomination.Denomination;
import ru.otus.exception.*;
import ru.otus.moneybox.MoneyBoxStorage;

public class AtmImpl implements Atm {
    private final MoneyBoxStorage storage;

    public AtmImpl(MoneyBoxStorage storage) {
        this.storage = storage;
    }

    @Override
    public void depositMoney(Denomination denomination, int quantity) throws IncorrectBanknotesQuantityException {
        if (quantity <= 0) {
            throw new IncorrectBanknotesQuantityException("Incorrect banknotes quantity: " + quantity);
        }
        storage.getStorageMoneyBox(denomination).depositBanknotes(quantity);
    }

    @Override
    public int withdrawMoney(int sum) throws WithdrawBanknotesCollectException, IncorrectSumWithdrawException {
        int sumWithdraw = sum;
        if (sumWithdraw <= 0) {
            throw new IncorrectSumWithdrawException("Incorrect sum input: " + sumWithdraw);
        }

        MoneyBoxStorage localStorage = new MoneyBoxStorage(Denomination.values());
        for (Denomination denomination : Denomination.values()) {
            if (storage.getStorageMoneyBox(denomination).getQuantity() > 0) {
                int banknoteCounter = Math.min(
                        sumWithdraw / denomination.getValue(),
                        storage.getStorageMoneyBox(denomination).getQuantity());
                sumWithdraw = sumWithdraw - banknoteCounter * denomination.getValue();
                localStorage.getStorageMoneyBox(denomination).depositBanknotes(banknoteCounter);
                storage.getStorageMoneyBox(denomination).withdrawBanknotes(banknoteCounter);
            }
        }

        if (sumWithdraw > 0) {
            for (Denomination denomination : Denomination.values()) {
                storage.getStorageMoneyBox(denomination)
                        .depositBanknotes(
                                localStorage.getStorageMoneyBox(denomination).getQuantity());
            }
            throw new WithdrawBanknotesCollectException("Error: Can't withdraw");
        }

        return sum;
    }

    @Override
    public int getBalance() {
        int sum = 0;
        for (Denomination denomination : Denomination.values()) {
            sum += denomination.getValue()
                    * storage.getStorageMoneyBox(denomination).getQuantity();
        }
        return sum;
    }
}
