package ru.otus.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private Map<K, V> cache = new WeakHashMap<>();
    List<HwListener<K, V>> listeners = new ArrayList<>();
    // Надо реализовать эти методы

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).notify(key, cache.get(key), "put");
        }
    }

    @Override
    public void remove(K key) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).notify(key, cache.get(key), "remove");
        }
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
