package com.mb.lab.banks.utils.map;

import java.util.Map;
import java.util.Set;

public interface MapSet<K, V> extends Map<K, Set<V>> {

    public void addValue(K key, V value);

    public boolean contains(K key, V value);

}
