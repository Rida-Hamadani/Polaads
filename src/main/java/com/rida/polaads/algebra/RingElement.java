package com.rida.polaads.algebra;

import com.rida.polaads.polynomials.RingPolynomial;

public interface RingElement {
    Ring<RingElement> getSet();

    RingElement getAdditiveNeutral();

    RingElement getMultiplicativeNeutral();

    RingElement add(RingElement that);

    RingElement multiply(RingElement that);

    RingElement multiply(Integer scalar);

    default RingElement getAdditiveInverse() {
        return multiply(-1);
    }
}