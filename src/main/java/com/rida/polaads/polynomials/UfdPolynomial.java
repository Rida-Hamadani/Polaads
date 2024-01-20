package com.rida.polaads.polynomials;

import com.rida.polaads.algebra.UfdElement;
import com.rida.polaads.algebra.UFD;
import com.rida.polaads.arithmetic.NumberTheory;
import com.rida.polaads.utils.Converter;

import java.util.*;
public class UfdPolynomial<T extends UfdElement> extends RingPolynomial<T> {
    private UFD<T> ufd;

    public UfdPolynomial(Map<Integer, T> pow_cof) {
        super(pow_cof);
    }

    public UFD<T> getUfd() {
        return ufd;
    }

    public T getContent() {
        if (Polynomials.ZERO.getMap().equals(pow_cof)) {
            return ufd.getAdditiveNeutral();
        }

        // int sign = pow_cof.get(getDegree()) > 0 ? 1 : -1;
        // return sign * NumberTheory.gcd(new ArrayList<>(pow_cof.values()));
        return (T) getUfd().gcd(new ArrayList<>(pow_cof.values()));
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

    public Boolean isSquareFree() {
        return RingPolynomial.gcd(this, getDerivative()).equals(Polynomials.ONE);
    }

    public RingPolynomial divide(Integer scalar) {
        if (scalar == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        pow_cof.forEach((k, v) -> {
            if (v % scalar != 0) {
                throw new IllegalArgumentException(scalar + " doesn't divide all the coefficients of " + this + ".");
            }
        });
        pow_cof.forEach((k, v) -> pow_cof.put(k, v / scalar));
        return this.clean();
    }

    public DivisionResult pseudoDivide(RingPolynomial that) {
        Map<Integer, Integer> quotientMap = new HashMap<>(), remainderMap = new HashMap<>(), tempMap = Converter.deepCopy(pow_cof);
        int deg1 = getDegree(), deg2 = that.getDegree(), quotientDegree = deg1 - deg2;

        if (pow_cof.equals(Polynomials.ZERO.getMap())) {
            return new DivisionResult(Polynomials.ZERO.getMap(), Polynomials.ZERO.getMap());
        }
        if (deg1 < deg2) {
            throw new IllegalArgumentException("Cannot divide by larger polynomial.");
        }

        for (int i = quotientDegree; i > -1; --i) {
            quotientMap.put(i, tempMap.getOrDefault(i + deg2, 0) * NumberTheory.power(that.pow_cof.getOrDefault(deg2, 0), i));
            for (int j = deg2 + i - 1; j > -1; --j) {
                tempMap.put(j, that.pow_cof.get(deg2) * tempMap.getOrDefault(j, 0) - tempMap.getOrDefault(i + deg2, 0) * that.pow_cof.getOrDefault(j - i, 0));
            }
        }
        for (int i = 0; i < deg2; ++i) {
            remainderMap.put(i, tempMap.getOrDefault(i, 0));
        }

        return new DivisionResult(quotientMap, remainderMap);
    }

    public static RingPolynomial gcd(RingPolynomial u, RingPolynomial v) {
        int deg1 = u.getDegree(), deg2 = v.getDegree(), d = NumberTheory.gcd(u.getContent(), v.getContent()), g = 1, h = 1, delta;

        if (deg1 == Integer.MIN_VALUE || deg2 == Integer.MIN_VALUE) {
            return u.add(v);
        }

        RingPolynomial temp = new RingPolynomial();
        DivisionResult uCopy = new DivisionResult(Converter.deepCopy(deg1 >= deg2 ? u.getMap() : v.getMap()));
        DivisionResult vCopy = new DivisionResult(Converter.deepCopy(deg1 < deg2 ? u.getMap() : v.getMap()));

        uCopy.setMap(uCopy.getPrimitive().getMap());
        vCopy.setMap(vCopy.getPrimitive().getMap());

        for (; ; ) {
            delta = uCopy.getDegree() - vCopy.getDegree();

            temp.setMap(Converter.deepCopy(uCopy.getMap())); // so thatCopy isn't changed during division
            RingPolynomial remainder = temp.pseudoDivide(vCopy).getRemainder();

            if (remainder.getMap().equals(Polynomials.ZERO.getMap())) {
                break;
            }
            if (remainder.getDegree() == 0) {
                vCopy.setMap(Polynomials.ONE.getMap());
                break;
            }

            uCopy.setMap(vCopy.getMap());
            vCopy.setMap(remainder.divide(g * NumberTheory.power(h, delta)).getMap());
            g = uCopy.getLeadingCoefficient();
            int oldh = h;
            h = NumberTheory.power(g, delta);
            for (int c = 0; c < delta; ++c) {
                h /= oldh;
            }
        }

        return vCopy.getPrimitive().multiply(d);
        // note that gcd of polynomials is unique up to a constant
        // this constant difference could lead to confusion
    }
}
