package com.rida.polaads.polynomials;

import com.rida.polaads.algebra.Ring;
import com.rida.polaads.algebra.RingElement;

import java.util.*;
import java.util.stream.Collectors;

public class RingPolynomial<T extends RingElement> implements RingElement {
    protected Map<Integer, T> pow_cof; // done this way to support sparse polynomials efficiently
    private Ring<T> ring;

    public RingPolynomial(Map<Integer, T> pow_cof) {
        if (pow_cof.isEmpty()) {
            throw new IllegalArgumentException("Polynomial can't be empty");
        }
        this.pow_cof = pow_cof;
        this.ring = (Ring<T>) pow_cof.entrySet().iterator().next().getValue().getSet();
    }

    public RingPolynomial(Ring<T> ring) {
        pow_cof = new HashMap<>();
        pow_cof.put(0, ring.getAdditiveNeutral());
    }

    protected RingPolynomial<T> clean() {
        pow_cof = pow_cof.entrySet().stream().filter(k -> k.getValue() != getAdditiveNeutral())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (pow_cof.isEmpty()) {
            pow_cof.put(0, getRing().getAdditiveNeutral());
        }

        return this;
    }

    public Map<Integer, T> getMap() {
        return clean().pow_cof;
    }

    public Ring<T> getRing() {
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

    public RingPolynomial<T> getDerivative() {
        RingPolynomial<T> der = new RingPolynomial<>(getRing());
        pow_cof.forEach(((k, v) -> der.pow_cof.put(k - 1, (T) v.multiply(k))));
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

    @Override
    public RingPolynomial getAdditiveNeutral() {
        return Polynomials.ZERO;
    }

    @Override
    public RingPolynomial getMultiplicativeNeutral() {
        return Polynomials.ONE;
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
            return new RingPolynomial(getRing());
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
        Map<Integer, T> sorted = new TreeMap<>(clean().getMap());

        sorted.forEach((k, v) -> {
            StringBuilder sb = new StringBuilder(4);

            if ((sj.length() > 0) && !v.equals(getRing().getAdditiveNeutral())) {
                sj.add("+");
            }
            if ((!v.equals(getRing().getMultiplicativeNeutral())) || k == 0) {
                sb.append(v);
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
