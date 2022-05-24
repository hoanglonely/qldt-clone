package com.mb.lab.banks.utils.map;

import java.math.BigDecimal;

public interface IncrementMap<K> {

    public BigDecimal get(K key);

    public double getDoubleValue(K key);

    public int getIntValue(K key);

    public void increment(K key, BigDecimal value);

    public void increment(K key, double value);

    public void increment(K key, int value);

}
