package com.mb.lab.banks.utils.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface MapDoubleKey<K1, K2, V> {

    public V get(K1 key1, K2 key2);

    public V put(K1 key1, K2 key2, V value);

    public void putAll(Map<? extends K1, ? extends Map<K2, V>> m);

    public boolean containsKey(K1 key1, K2 key2);

    public boolean containsValue(V value);

    public List<V> values();

    public Set<Entry<K1, Map<K2, V>>> entrySet();

    public Map<K1, Map<K2, V>> getMap();

}
