package com.polynomials;

import java.util.Map;

public class Polynomials {
    private static final Map<Integer, Integer> zeroMap = Map.of(0, 0);
    private static final Map<Integer, Integer> oneMap = Map.of(0, 1);

    public static final Polynomial ZERO = new Polynomial(zeroMap);
    public static final Polynomial ONE = new Polynomial(oneMap);
}
