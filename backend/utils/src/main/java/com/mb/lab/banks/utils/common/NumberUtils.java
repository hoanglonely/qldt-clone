package com.mb.lab.banks.utils.common;

public class NumberUtils {

    private static Integer INT_ZERO = new Integer(0);
    private static Long LONG_ZERO = new Long(0);

    public static final boolean isEquals(Long long1, Long long2) {
        if (long1 == null && long2 == null) {
            return true;
        }

        if (long1 == null || long2 == null) {
            return false;
        }

        return long1.equals(long2);
    }

    public static final boolean isEmptyOrZero(Long number) {
        return number == null || LONG_ZERO.compareTo(number) == 0;
    }

    public static final boolean isEmptyOrZero(Integer number) {
        return number == null || INT_ZERO.compareTo(number) == 0;
    }

    public static final boolean isPositive(Integer number) {
        return number != null && INT_ZERO.compareTo(number) < 0;
    }
}
