package com.mb.lab.banks.utils.map;

import java.math.BigDecimal;
import java.util.HashMap;

public class IncrementHashMap<K> extends HashMap<K, BigDecimal> implements IncrementMap<K> {

    private static final long serialVersionUID = -2843736955958820167L;

    public IncrementHashMap() {
        super();
    }

    @Override
    public BigDecimal get(Object key) {
        BigDecimal value = super.get(key);
        return value == null ? BigDecimal.ZERO : value;
    }

    @Override
    public double getDoubleValue(Object key) {
        return get(key).doubleValue();
    }

    @Override
    public int getIntValue(Object key) {
        return get(key).intValue();
    }

    @Override
    public void increment(K key, BigDecimal value) {
        if (value != null) {
            BigDecimal old = get(key);
            value = old.add(value);
            super.put(key, value);
        }
    }

    @Override
    public void increment(K key, double value) {
        increment(key, BigDecimal.valueOf(value));
    }

    @Override
    public void increment(K key, int value) {
        increment(key, BigDecimal.valueOf(value));
    }

}
