package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы

    private final Map<K, V> storage;
    private final boolean useCashe;
    private final List<HwListener<K, V>> listeners;
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    public MyCache(Map<K, V> storage, boolean useCashe) {
        this.storage = storage;
        this.useCashe = useCashe;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        if (useCashe){
            logger.atDebug().setMessage("put to cache : key => %s".formatted(key)).log();
            storage.put(key, value);
        }
    }

    @Override
    public void remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(K key) {
        if (useCashe == false){
            return null;
        }
        logger.atDebug().setMessage("get from cache : key => %s".formatted(key)).log();
        return storage.putIfAbsent(key, null);
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
