package ru.otus.moneybox;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import ru.otus.denomination.Denomination;

public class MoneyBoxStorage {
    private final Map<Denomination, MoneyBoxImpl> storage = new TreeMap<>(Collections.reverseOrder());

    public void createStorage() {
        for (Denomination denomination : Denomination.values()) {
            storage.put(denomination, new MoneyBoxImpl());
        }
    }

    public Map<Denomination, MoneyBoxImpl> getStorage() {
        return storage;
    }
}
