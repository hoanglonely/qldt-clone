package com.mb.lab.banks.utils.map;

import java.util.List;
import java.util.Map;

public interface MapList<K, V> extends Map<K, List<V>> {

    public void addValue(K key, V value);

}
