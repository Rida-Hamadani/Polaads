package com.polaads.polynomials;

import com.polaads.polynomials.Polynomial;

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