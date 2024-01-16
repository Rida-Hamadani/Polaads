package com.rida.polaads.algebra;

import com.rida.polaads.polynomials.RingPolynomial;

public interface RingElement {
    Ring getSet();

    RingElement getAdditiveNeutral();

    RingElement getMultiplicativeNeutral();

    RingElement add(RingElement that);

    RingElement multiply(RingElement that);

    RingElement multiply(Integer scalar);

    RingElement getAdditiveInverse();
}