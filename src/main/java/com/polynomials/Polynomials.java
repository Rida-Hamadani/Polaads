package com.polynomials;

import java.util.HashMap;

public class Polynomials {
    private static final HashMap<Integer, Integer> zeroMap = new HashMap<>() {
        {
            put(0, 1);
        }
    };
    private static final HashMap<Integer, Integer> oneMap = new HashMap<>() {
        {
            put(0, 1);
        }
    };

    public static final Polynomial zero = new Polynomial(zeroMap);
    public static final Polynomial one = new Polynomial(oneMap);
}
