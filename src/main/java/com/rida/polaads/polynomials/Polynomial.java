package com.rida.polaads.polynomials;

import com.rida.polaads.utils.Converter;
import com.rida.polaads.arithmetic.NumberTheory;

import java.util.*;
import java.util.stream.Collectors;

public class Polynomial {
    private Map<Integer, Integer> pow_cof; // done this way to support sparse polynomials efficiently

    public Polynomial() {
        this.pow_cof = new HashMap<>();
        this.pow_cof.put(0, 0);
    }

    public Polynomial(Map<Integer, Integer> pow_cof) {
        this.pow_cof = pow_cof;
    }

    public Polynomial(ArrayList<Integer> powers, ArrayList<Integer> coefs) {
        this(Converter.connect(powers, coefs));
    }

    protected Polynomial clean() {
        pow_cof = pow_cof.entrySet().stream().filter(k -> k.getValue() != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (pow_cof.isEmpty()) {
            pow_cof.put(0, 0);
        }

        return this;
    }

    public Map<Integer, Integer> getMap() {
        return clean().pow_cof;
    }

    public Polynomial setMap(Map<Integer, Integer> newMap) {
        pow_cof = newMap;
        return this;
    }

    public Integer getDegree() {
        clean();
        return pow_cof.equals(Polynomials.ZERO.getMap())
                ? Integer.MIN_VALUE
                : Collections.max(pow_cof.keySet());
    }

    public Integer getLeadingCoefficient() {
        Integer deg = getDegree();
        if (deg == Integer.MIN_VALUE) {
            return 0;
        }
        return pow_cof.get(deg);
    }

    public Integer getContent() {
        if (Polynomials.ZERO.getMap().equals(pow_cof)) {
            return 0;
        }

        int sign = pow_cof.get(getDegree()) > 0 ? 1 : -1;
        return sign * NumberTheory.gcd(new ArrayList<>(pow_cof.values()));
    }

    public Polynomial getPrimitive() {
        if (Polynomials.ZERO.getMap().equals(pow_cof)) {
            return new Polynomial();
        }
        Polynomial primitive = new Polynomial(Converter.deepCopy(pow_cof));
        primitive.divide(getContent());
        return primitive;
    }

    public Polynomial getDerivative() {
        Polynomial der = new Polynomial();
        pow_cof.forEach(((k, v) -> der.pow_cof.put(k - 1, v * k)));
        der.pow_cof.remove(-1);
        return der.clean();
    }

    public Boolean isPrimitive() {
        return Math.abs(getContent()) == 1;
    }

    public Boolean isSquareFree() {
        return Polynomial.gcd(this, getDerivative()).equals(Polynomials.ONE);
    }

    public Polynomial add(Polynomial that) {
        that.pow_cof.forEach((k, v) -> pow_cof.merge(k, v, Integer::sum));
        return clean();
    }

    public Polynomial subtract(Polynomial that) {
        if (equals(that))
            return new Polynomial();
        add(that.multiply(-1));
        that.multiply(-1); // so that isn't mutated
        return clean();
    }

    public Polynomial multiply(Integer scalar) {
        pow_cof.forEach((k, v) -> pow_cof.put(k, scalar * v));
        return clean();
    }

    public Polynomial multiply(Polynomial that) {
        HashMap<Integer, Integer> product = new HashMap<>();
        that.pow_cof.forEach((k1, v1) -> pow_cof.forEach((k2, v2) -> product.merge(k1 + k2, v1 * v2, Integer::sum)));
        return setMap(product).clean();
    }

    public Polynomial divide(Integer scalar) {
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

    public DivisionResult divide(Polynomial that) {
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

    public DivisionResult pseudoDivide(Polynomial that) {
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

    public static Polynomial gcd(Polynomial u, Polynomial v) {
        int deg1 = u.getDegree(),
                deg2 = v.getDegree(),
                d = NumberTheory.gcd(u.getContent(), v.getContent()),
                g = 1,
                h = 1,
                delta;

        if (deg1 == Integer.MIN_VALUE || deg2 == Integer.MIN_VALUE) {
            return u.add(v);
        }
        
        Polynomial temp = new Polynomial();
        DivisionResult uCopy = new DivisionResult(Converter.deepCopy(deg1 >= deg2 ? u.getMap() : v.getMap()));
        DivisionResult vCopy = new DivisionResult(Converter.deepCopy(deg1 < deg2 ? u.getMap() : v.getMap()));

        uCopy.setMap(uCopy.getPrimitive().getMap());
        vCopy.setMap(vCopy.getPrimitive().getMap());

        for (;;) {
            delta = uCopy.getDegree() - vCopy.getDegree();

            temp.setMap(Converter.deepCopy(uCopy.getMap())); // so thatCopy isn't changed during division
            Polynomial remainder = temp.pseudoDivide(vCopy).getRemainder();

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
        Polynomial that = (Polynomial) o;
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
