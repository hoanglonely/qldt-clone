package com.mb.lab.banks.utils.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.CollectionUtils;

/**
 *
 * @author kimhatrung
 */
public class HashMapDoubleKey<K1, K2, V> implements MapDoubleKey<K1, K2, V> {

    @SuppressWarnings({ "rawtypes" })
    public static final HashMapDoubleKey EMPTY_MAP = new HashMapDoubleKey<>(Collections.emptyMap());

    @SuppressWarnings("unchecked")
    public static final <K1, K2, V> HashMapDoubleKey<K1, K2, V> emptyMap() {
        return EMPTY_MAP;
    }

    private Map<K1, Map<K2, V>> map;

    public HashMapDoubleKey(Map<K1, Map<K2, V>> map) {
        if (map == null) {
            this.map = new HashMap<K1, Map<K2, V>>();
        } else {
            this.map = map;
        }
    }

    public HashMapDoubleKey() {
        this(null);
    }

    @Override
    public V get(K1 key1, K2 key2) {
        Map<K2, V> map2 = map.get(key1);
        if (map2 != null) {
            return map2.get(key2);
        } else {
            return null;
        }
    }

    @Override
    public V put(K1 key1, K2 key2, V value) {
        Map<K2, V> map2 = map.get(key1);
        if (map2 == null) {
            map2 = new HashMap<K2, V>();
            map2.put(key2, value);
            map.put(key1, map2);
            return null;

        } else {
            return map2.put(key2, value);
        }
    }

    public boolean isEmpty() {
        if (map.isEmpty()) {
            return true;
        } else {
            for (Map<K2, V> map2 : map.values()) {
                if (!map2.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean containsKey(K1 key1, K2 key2) {
        Map<K2, V> map2 = map.get(key1);
        if (map2 != null) {
            return map2.containsKey(key2);
        } else {
            return false;
        }
    }

    @Override
    public boolean containsValue(V value) {
        for (Entry<K1, Map<K2, V>> entry : map.entrySet()) {
            if (entry.getValue().containsValue(value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void putAll(Map<? extends K1, ? extends Map<K2, V>> m) {
        map.putAll(m);
    }

    @Override
    public List<V> values() {
        Collection<Map<K2, V>> _values = map.values();

        if (CollectionUtils.isEmpty(_values)) {
            return Collections.emptyList();
        }

        LinkedList<V> values = new LinkedList<>();
        for (Map<K2, V> map : _values) {
            values.addAll(map.values());
        }

        return values;
    }

    @Override
    public Set<Entry<K1, Map<K2, V>>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Map<K1, Map<K2, V>> getMap() {
        return this.map;
    }

}
