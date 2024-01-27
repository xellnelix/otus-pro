package ru.otus.atm;

import ru.otus.denomination.Denomination;
import ru.otus.exception.*;
import ru.otus.moneybox.MoneyBoxStorage;

public class AtmImpl implements Atm {
    private final MoneyBoxStorage storage;

    public AtmImpl(MoneyBoxStorage storage) {
        this.storage = storage;
        storage.createStorage();
    }

    @Override
    public void depositMoney(Denomination denomination, int quantity) throws IncorrectBanknotesQuantityException {
        if (quantity <= 0) {
            throw new IncorrectBanknotesQuantityException("Incorrect banknotes quantity: " + quantity);
        }
        storage.getStorage().get(denomination).depositBanknotes(quantity);
    }

    @Override
    public void withdrawMoney(int sum) throws WithdrawBanknotesCollectException, IncorrectSumWithdrawException {
        if (sum <= 0) {
            throw new IncorrectSumWithdrawException("Incorrect sum input: " + sum);
        }

        MoneyBoxStorage localStorage = new MoneyBoxStorage();
        localStorage.createStorage();
        for (Denomination denomination : storage.getStorage().keySet()) {
            if (storage.getStorage().get(denomination).getQuantity() == 0) {
                continue;
            }
            int banknoteCounter = Math.min(
                    sum / denomination.getValue(),
                    storage.getStorage().get(denomination).getQuantity());
            sum = sum - banknoteCounter * denomination.getValue();
            localStorage.getStorage().get(denomination).depositBanknotes(banknoteCounter);
            storage.getStorage().get(denomination).withdrawBanknotes(banknoteCounter);
        }

        if (sum > 0) {
            for (Denomination denomination : storage.getStorage().keySet()) {
                storage.getStorage()
                        .get(denomination)
                        .depositBanknotes(
                                localStorage.getStorage().get(denomination).getQuantity());
            }
            throw new WithdrawBanknotesCollectException("Error: Can't withdraw");
        }
    }

    @Override
    public int showBalance() {
        int sum = 0;
        for (Denomination denomination : Denomination.values()) {
            sum += denomination.getValue()
                    * storage.getStorage().get(denomination).getQuantity();
        }
        return sum;
    }
}
