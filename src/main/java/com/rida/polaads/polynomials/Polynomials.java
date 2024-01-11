package com.rida.polaads.polynomials;

import java.util.Map;

public class Polynomials {
    private static final Map<Integer, Integer> zeroMap = Map.of(0, 0);

    private static final Map<Integer, Integer> oneMap = Map.of(0, 1);

    private static final Map<Integer, Integer> idMap = Map.of(1, 1);

    public static final Polynomial ZERO = new Polynomial(zeroMap);

    public static final Polynomial ONE = new Polynomial(oneMap);

    public static final Polynomial IDENTITY = new Polynomial(idMap);
}
