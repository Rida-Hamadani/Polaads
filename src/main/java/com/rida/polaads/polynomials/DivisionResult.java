package com.rida.polaads.polynomials;

import java.util.*;

public class DivisionResult extends RingPolynomial {
    private RingPolynomial remainder;

    public DivisionResult(Map<Integer, Integer> quotientMap) {
        super(quotientMap);
    }

    public DivisionResult(Map<Integer, Integer> quotientMap, Map<Integer, Integer> remainderMap) {
        super(quotientMap);
        this.remainder = new RingPolynomial(remainderMap).clean();
    }

    public RingPolynomial getRemainder() {
        return remainder;
    }
}