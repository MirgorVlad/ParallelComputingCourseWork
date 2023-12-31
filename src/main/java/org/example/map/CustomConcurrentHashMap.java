package org.example.map;

import java.util.HashMap;
import java.util.Map;

public class CustomConcurrentHashMap<K, V> {
    private final int numberOfSegments = 16;
    private final Map<K, V>[] segments;


    public CustomConcurrentHashMap() {
        segments = new HashMap[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            segments[i] = new HashMap<>();
        }
    }

    private int getSegment(K key) {
        return Math.abs(key.hashCode() % numberOfSegments);
    }

    public void put(K key, V value) {
        int segment = getSegment(key);
        synchronized (segments[segment]) {
            segments[segment].put(key, value);
        }
    }

    public V get(K key) {
        int segment = getSegment(key);
        synchronized (segments[segment]) {
            return segments[segment].get(key);
        }
    }

    public void remove(K key) {
        int segment = getSegment(key);
        synchronized (segments[segment]) {
            segments[segment].remove(key);
        }
    }
}

