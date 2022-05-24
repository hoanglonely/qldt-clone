package com.mb.lab.banks.utils.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HashMapList<K, V> extends HashMap<K, List<V>> implements MapList<K, V> {

    private static final long serialVersionUID = 4775708207935184013L;

    @Override
    public void addValue(K key, V value) {
        List<V> list = super.get(key);

        if (list == null) {
            list = new LinkedList<>();
        }

        list.add(value);

        super.put(key, list);
    }

    @Override
    public List<V> get(Object key) {
        List<V> list = super.get(key);

        if (list == null) {
            return Collections.emptyList();
        }

        return list;
    }

}
