package com.mb.lab.banks.utils.map;

import java.io.Serializable;
import java.math.BigDecimal;

public interface IncrementMapDoubleKey<K1, K2> extends Serializable {

    public BigDecimal get(K1 key1, K2 key2);

    public double getDoubleValue(K1 key1, K2 key2);

    public int getIntValue(K1 key1, K2 key2);

    public void increment(K1 key1, K2 key2, BigDecimal value);

    public void increment(K1 key1, K2 key2, double value);

    public void increment(K1 key1, K2 key2, int value);

}
