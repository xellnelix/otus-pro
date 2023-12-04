package ru.otus;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> tree = new TreeMap<>(Comparator.comparing(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstEntry = tree.firstEntry();
        if (firstEntry == null) {
            return null;
        }
        return Map.entry(new Customer(firstEntry.getKey()), firstEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> findEntry = tree.higherEntry(customer);
        if (findEntry == null) {
            return null;
        }
        return Map.entry(new Customer(findEntry.getKey()), findEntry.getValue());
    }

    public void add(Customer customer, String data) {
        tree.put(customer, data);
    }
}
