package com.rida.polaads.polynomials;

import java.util.*;

public class DivisionResult extends Polynomial {
    private Polynomial remainder;

    public DivisionResult(Map<Integer, Integer> quotientMap) {
        super(quotientMap);
    }

    public DivisionResult(Map<Integer, Integer> quotientMap, Map<Integer, Integer> remainderMap) {
        super(quotientMap);
        this.remainder = new Polynomial(remainderMap).clean();
    }

    public Polynomial getRemainder() {
        return remainder;
    }
}