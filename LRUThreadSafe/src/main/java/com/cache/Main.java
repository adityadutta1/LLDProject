package com.cache;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        LRUCache<String, Integer> cache = new LRUCache<>(3);

        // Adding elements
        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);

        // Access element (will move to most recently used)
        System.out.println("Value for key 'B': " + cache.get("B"));

        // Add new element, should evict least recently used
        cache.put("D", 4);

        // Print final cache state
        cache.printCache();
    }
}



