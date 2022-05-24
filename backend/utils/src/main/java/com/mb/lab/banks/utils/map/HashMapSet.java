package com.mb.lab.banks.utils.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HashMapSet<K, V> extends HashMap<K, Set<V>> implements MapSet<K, V> {

    private static final long serialVersionUID = 4775708207935184013L;

    @Override
    public void addValue(K key, V value) {
        Set<V> list = super.get(key);

        if (list == null) {
            list = new HashSet<>();
        }

        list.add(value);

        super.put(key, list);
    }

    @Override
    public boolean contains(K key, V value) {
        Set<V> list = super.get(key);

        if (list == null) {
            return false;
        }

        return list.contains(value);
    }

}
