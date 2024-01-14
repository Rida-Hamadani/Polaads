package com.rida.polaads.polynomials;

import java.util.Map;

public class Polynomials {
    private static final Map<Integer, Integer> zeroMap = Map.of(0, 0);

    private static final Map<Integer, Integer> oneMap = Map.of(0, 1);

    private static final Map<Integer, Integer> idMap = Map.of(1, 1);

    public static final RingPolynomial ZERO = new RingPolynomial(zeroMap);

    public static final RingPolynomial ONE = new RingPolynomial(oneMap);

    public static final RingPolynomial IDENTITY = new RingPolynomial(idMap);
}
