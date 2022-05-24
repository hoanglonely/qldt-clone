package com.mb.lab.banks.utils.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapWithDefault<K, V> implements Map<K, V> {

    private Map<K, V> map;
    private V defaultValue;

    public MapWithDefault(Map<K, V> map, V defaultValue) {
        this.map = map == null ? Collections.emptyMap() : map;
        this.defaultValue = defaultValue;
    }

    public MapWithDefault(V defaultValue) {
        this(new HashMap<>(), defaultValue);
    }

    public void replaceAll(Map<K, V> map) {
        this.map = map == null ? Collections.emptyMap() : map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        V value = map.get(key);
        return value == null ? this.defaultValue : value;
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

}
