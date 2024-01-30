package ru.otus.moneybox;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import ru.otus.denomination.Denomination;

public class MoneyBoxStorage {
    private final Map<Denomination, MoneyBoxImpl> storage = new TreeMap<>(Collections.reverseOrder());

    public MoneyBoxStorage(Denomination[] denominations) {
        for (Denomination denomination : denominations) {
            storage.put(denomination, new MoneyBoxImpl());
        }
    }

    public MoneyBoxImpl getStorageMoneyBox(Denomination denomination) {
        return storage.get(denomination);
    }
}
