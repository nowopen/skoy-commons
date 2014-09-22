package com.nowopen.encrypt;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache {
    private int cacheSize = 0;
    private float loadFactor = 0.75f;
    private LinkedHashMap map = null;

    public LRUCache(int cacheSize) {
        this.cacheSize = cacheSize;
        map = new LinkedHashMap(cacheSize, loadFactor, true) {
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > LRUCache.this.cacheSize;
            }
        };
    }

    public synchronized void clear() {
        map.clear();
    }

    public synchronized Object get(Object key) {
        return map.get(key);
    }

    public synchronized void put(Object key, Object value) {
        map.put(key, value);
    }

    public synchronized Object remove(Object key) {
        return map.remove(key);
    }

    public synchronized int size() {
        return map.size();
    }

    public synchronized Collection values() {
        return map.values();
    }

}
