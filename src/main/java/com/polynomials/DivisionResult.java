package com.polynomials;

import java.util.*;

public class DivisionResult extends Polynomial {
    private Polynomial remainder;

    public DivisionResult(HashMap<Integer, Integer> quotientMap) {
        super(quotientMap);
    }

    public DivisionResult(HashMap<Integer, Integer> quotientMap, HashMap<Integer, Integer> remainderMap) {
        super(quotientMap);
        this.remainder = new Polynomial(remainderMap).clean();
    }

    public Polynomial getRemainder() {
        return remainder;
    }
}