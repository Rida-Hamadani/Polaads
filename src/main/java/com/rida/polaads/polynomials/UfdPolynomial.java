package com.rida.polaads.polynomials;

import com.rida.polaads.utils.Converter;

import java.util.ArrayList;

public class UfdPolynomial<T> extends RingPolynomial<T> {
    public T getContent() {
        if (Polynomials.ZERO.getMap().equals(pow_cof)) {
            return (T) ring.getAdditiveNeutral();
        }

        // int sign = pow_cof.get(getDegree()) > 0 ? 1 : -1;
        // return sign * NumberTheory.gcd(new ArrayList<>(pow_cof.values()));
        return (T) gcd(new ArrayList<>(pow_cof.values()));
    }

    public RingPolynomial getPrimitive() {
        if (Polynomials.ZERO.getMap().equals(pow_cof)) {
            return new RingPolynomial();
        }
        RingPolynomial primitive = new RingPolynomial(Converter.deepCopy(pow_cof));
        primitive.divide(getContent());
        return primitive;
    }

    public Boolean isPrimitive() {
        return Math.abs(getContent()) == 1;
    }
}
