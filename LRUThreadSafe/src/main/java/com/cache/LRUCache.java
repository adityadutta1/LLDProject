package com.cache;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private final int capacity;
    private final ConcurrentHashMap<K, V> cache;
    private final LinkedHashMap<K, V> order;
    private final ReentrantReadWriteLock lock;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>(capacity);
        this.order = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
        this.lock = new ReentrantReadWriteLock();
    }

    public V get(K key) {
        lock.readLock().lock();
        try {
            V value = cache.get(key);
            if (value != null) {
                // Update access order
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    order.get(key);
                } finally {
                    lock.writeLock().unlock();
                    lock.readLock().lock();
                }
            }
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
            order.put(key, value);

            // If order map has exceeded capacity, remove eldest entry from cache
            if (order.size() > capacity) {
                K eldestKey = order.keySet().iterator().next();
                cache.remove(eldestKey);
                order.remove(eldestKey);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(K key) {
        lock.writeLock().lock();
        try {
            cache.remove(key);
            order.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
            order.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // For testing/debugging purposes
    public void printCache() {
        lock.readLock().lock();
        try {
            System.out.println("Cache contents in order of recent access:");
            for (Map.Entry<K, V> entry : order.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}