package com.mb.lab.banks.utils.map;

import java.math.BigDecimal;

public class IncrementHashMapDoubleKey<K1, K2> extends HashMapDoubleKey<K1, K2, BigDecimal> implements IncrementMapDoubleKey<K1, K2> {

    private static final long serialVersionUID = -2843736955958820167L;

    public IncrementHashMapDoubleKey() {
        super();
    }

    @Override
    public BigDecimal get(K1 key1, K2 key2) {
        BigDecimal value = super.get(key1, key2);
        return value == null ? BigDecimal.ZERO : value;
    }

    @Override
    public double getDoubleValue(K1 key1, K2 key2) {
        return get(key1, key2).doubleValue();
    }

    @Override
    public int getIntValue(K1 key1, K2 key2) {
        return get(key1, key2).intValue();
    }

    @Override
    public void increment(K1 key1, K2 key2, BigDecimal value) {
        if (value != null) {
            BigDecimal old = get(key1, key2);
            value = old.add(value);
            super.put(key1, key2, value);
        }
    }

    @Override
    public void increment(K1 key1, K2 key2, double value) {
        increment(key1, key2, BigDecimal.valueOf(value));
    }

    @Override
    public void increment(K1 key1, K2 key2, int value) {
        increment(key1, key2, BigDecimal.valueOf(value));
    }

}
