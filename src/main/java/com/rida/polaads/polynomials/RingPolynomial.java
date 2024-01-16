package com.rida.polaads.polynomials;

import com.rida.polaads.algebra.Ring;
import com.rida.polaads.algebra.RingElement;
import com.rida.polaads.utils.Converter;
import com.rida.polaads.arithmetic.NumberTheory;

import java.util.*;
import java.util.stream.Collectors;

public class RingPolynomial<T extends RingElement> implements RingElement {
    private Map<Integer, T> pow_cof; // done this way to support sparse polynomials efficiently
    private Ring ring;

    public RingPolynomial(Map<Integer, T> pow_cof) {
        if (pow_cof.isEmpty()) {
            throw new IllegalArgumentException("Polynomial can't be empty");
        }
        this.pow_cof = pow_cof;
        this.ring = pow_cof.entrySet().iterator().next().getValue().getSet();
    }

    protected RingPolynomial<T> clean() {
        pow_cof = pow_cof.entrySet().stream().filter(k -> k.getValue() != getAdditiveNeutral())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (pow_cof.isEmpty()) {
            pow_cof.put(0, (T) getRing().getAdditiveNeutral());
        }

        return this;
    }

    public Map<Integer, T> getMap() {
        return clean().pow_cof;
    }

    public Ring getRing() {
        return ring;
    }

    public RingPolynomial<T> setMap(Map<Integer, T> newMap) {
        pow_cof = newMap;
        return this;
    }

    public Integer getDegree() {
        clean();
        return pow_cof.equals(Polynomials.ZERO.getMap())
                ? Integer.MIN_VALUE
                : Collections.max(pow_cof.keySet());
    }

    public T getLeadingCoefficient() {
        Integer deg = getDegree();
        if (deg == Integer.MIN_VALUE) {
            return (T) ring.getAdditiveNeutral();
        }
        return pow_cof.get(deg);
    }

    public RingPolynomial getDerivative() {
        RingPolynomial der = new RingPolynomial();
        pow_cof.forEach(((k, v) -> der.pow_cof.put(k - 1, v * k)));
        der.pow_cof.remove(-1);
        return der.clean();
    }

    @Override
    public Ring getSet() {
        return new Ring() {
            @Override
            public RingElement getAdditiveNeutral() {
                return Polynomials.ZERO;
            }

            @Override
            public RingElement getMultiplicativeNeutral() {
                return Polynomials.ONE;
            }
        };
    }

    public Boolean isSquareFree() {
        return RingPolynomial.gcd(this, getDerivative()).equals(Polynomials.ONE);
    }

    @Override
    public RingPolynomial add(RingElement that) {
        if (!(that instanceof Polynomial)) {
            throw new IllegalArgumentException("Must be a polynomial");
        }
        RingPolynomial<T> thatPoly = (RingPolynomial<T>) that;
        thatPoly.getMap().forEach((k, v) -> pow_cof.merge((Integer) k, (T) v, (a, b) -> (T) a.add(b)));
        return clean();
    }

    public RingPolynomial subtract(RingPolynomial that) {
        if (equals(that))
            return new RingPolynomial();
        add(that.multiply(-1));
        that.multiply(-1); // so that isn't mutated
        return clean();
    }

    @Override
    public RingPolynomial multiply(Integer scalar) {
        pow_cof.forEach((k, v) -> pow_cof.put(k, (T) v.multiply(scalar)));
        return clean();
    }

    @Override
    public RingPolynomial multiply(RingElement that) {
        if (!(that instanceof Polynomial)) {
            throw new IllegalArgumentException("Must be a polynomial");
        }
        RingPolynomial<T> thatPoly = (RingPolynomial<T>) that;
        HashMap<Integer, T> product = new HashMap<>();
        thatPoly.getMap().forEach((k1, v1) -> pow_cof.forEach((k2, v2) -> product.merge(k1 + k2, (T) v1.multiply(v2), (a,b) -> (T) a.add(b))));
        return setMap(product).clean();
    }

    public RingPolynomial divide(Integer scalar) {
        if (scalar == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        pow_cof.forEach((k, v) -> {
            if (v % scalar != 0) {
                throw new IllegalArgumentException(
                        scalar + " doesn't divide all the coefficients of " + this + ".");
            }
        });
        pow_cof.forEach((k, v) -> pow_cof.put(k, v / scalar));
        return this.clean();
    }

    public DivisionResult divide(RingPolynomial that) {
        int deg1 = getDegree(),
                deg2 = that.getDegree(),
                quotientDegree = deg1 - deg2;
        Map<Integer, Integer> quotientMap = new HashMap<>(),
                remainderMap = new HashMap<>(),
                tempMap = Converter.deepCopy(pow_cof);

        if (pow_cof.equals(Polynomials.ZERO.getMap())) {
            return new DivisionResult(Polynomials.ZERO.getMap(), Polynomials.ZERO.getMap());
        }
        if (that.pow_cof.equals(Polynomials.ZERO.getMap())) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        if (deg1 < deg2) {
            throw new IllegalArgumentException("Cannot divide by larger polynomial.");
        }
        if (pow_cof.get(deg1) % that.pow_cof.get(deg2) != 0) {
            throw new IllegalArgumentException(
                    "The leading coefficient of the dividend should be divisible by that of the divisor.");
        }

        for (int i = quotientDegree; i > -1; --i) {
            quotientMap.put(i, tempMap.getOrDefault(i + deg2, 0) / that.pow_cof.getOrDefault(deg2, 0));
            for (int j = deg2 + i - 1; j > i - 1; --j) {
                tempMap.put(j, tempMap.getOrDefault(j, 0) - quotientMap.get(i) * that.pow_cof.getOrDefault(j - i, 0));
            }
        }
        for (int i = 0; i < deg2; ++i) {
            remainderMap.put(i, tempMap.getOrDefault(i, 0));
        }

        return new DivisionResult(quotientMap, remainderMap);
    }

    public DivisionResult pseudoDivide(RingPolynomial that) {
        Map<Integer, Integer> quotientMap = new HashMap<>(),
                remainderMap = new HashMap<>(),
                tempMap = Converter.deepCopy(pow_cof);
        int deg1 = getDegree(),
                deg2 = that.getDegree(),
                quotientDegree = deg1 - deg2;

        if (pow_cof.equals(Polynomials.ZERO.getMap())) {
            return new DivisionResult(Polynomials.ZERO.getMap(), Polynomials.ZERO.getMap());
        }
        if (deg1 < deg2) {
            throw new IllegalArgumentException("Cannot divide by larger polynomial.");
        }

        for (int i = quotientDegree; i > -1; --i) {
            quotientMap.put(i, tempMap.getOrDefault(i + deg2, 0)
                    * NumberTheory.power(that.pow_cof.getOrDefault(deg2, 0), i));
            for (int j = deg2 + i - 1; j > -1; --j) {
                tempMap.put(j, that.pow_cof.get(deg2) * tempMap.getOrDefault(j, 0)
                        - tempMap.getOrDefault(i + deg2, 0) * that.pow_cof.getOrDefault(j - i, 0));
            }
        }
        for (int i = 0; i < deg2; ++i) {
            remainderMap.put(i, tempMap.getOrDefault(i, 0));
        }

        return new DivisionResult(quotientMap, remainderMap);
    }

    public static RingPolynomial gcd(RingPolynomial u, RingPolynomial v) {
        int deg1 = u.getDegree(),
                deg2 = v.getDegree(),
                d = NumberTheory.gcd(u.getContent(), v.getContent()),
                g = 1,
                h = 1,
                delta;

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
//    public List<Polynomial> factorizeBerlekamp(Integer prime) {
//        if (!isSquareFree()) {
//            throw new IllegalArgumentException(this + " is not square free.");
//        }
//        if (!NumberTheory.isPrime(prime)) {
//            throw new IllegalArgumentException(prime + " is not prime.");
//        }
//
//        List<Polynomial> factors = new ArrayList<>();
//        return factors;

//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RingPolynomial that = (RingPolynomial) o;
        return Objects.equals(pow_cof, that.pow_cof);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pow_cof);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");
        Map<Integer, Integer> sorted = new TreeMap<>(clean().getMap());

        sorted.forEach((k, v) -> {
            StringBuilder sb = new StringBuilder(4);

            if ((sj.length() > 0 || v < 0) && v != 0) {
                sj.add(v > 0 ? "+" : "-");
            }
            if ((v != 1 && v != -1) || k == 0) {
                sb.append(Math.abs(v));
            }
            if (k != 0) {
                sb.append('x');
                if (k != 1) {
                    sb.append('^').append(k);
                }
            }
            sj.add(sb.toString());
        });

        return sj.toString();
    }
}
