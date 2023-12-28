package com.polynomials;

import java.util.*;

public class Polynomial {
    private HashMap<Integer, Integer> pow_cof; // done this way to support sparse polynomials efficiently

    public Polynomial() {
        this.pow_cof = new HashMap<>();
        this.pow_cof.put(0, 0);
    }

    public Polynomial(HashMap<Integer, Integer> pow_cof) {
        this.pow_cof = pow_cof;
    }

    public Polynomial(ArrayList<Integer> powers, ArrayList<Integer> coefs) {
        this(Converter.connect(powers, coefs));
    }

    private Polynomial clean() {
        pow_cof.values().removeIf(v -> v == 0);
        if (pow_cof.size() == 0)
            pow_cof.put(0, 0);
        return this;
    }

    public HashMap<Integer, Integer> getMap() {
        return pow_cof;
    }

    public Polynomial setMap(HashMap<Integer, Integer> newMap) {
        pow_cof = newMap;
        return this;
    }

    public Integer getDegree() {
        clean();
        HashMap<Integer, Integer> zero = new HashMap<>() {
            {
                put(0, 0);
            }
        };
        return pow_cof.equals(zero)
                ? Integer.MIN_VALUE
                : Collections.max(pow_cof.keySet());
    }

    public Polynomial add(Polynomial that) {
        that.pow_cof.forEach((k, v) -> pow_cof.merge(k, v, (a, b) -> a + b));
        return clean();
    }

    public Polynomial subtract(Polynomial that) {
        if (equals(that)) return new Polynomial();
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
        that.pow_cof.forEach((k1, v1) -> pow_cof.forEach((k2, v2) -> product.merge(k1 + k2, v1 * v2, (a, b) -> a + b)));
        return setMap(product).clean();
    }

    public DivisionResult divide(Polynomial that) {
        Integer deg1 = getDegree(),
                deg2 = that.getDegree(),
                quotientDegree = deg1 - deg2;
        HashMap<Integer, Integer> quotientMap = new HashMap<>(),
                remainderMap = new HashMap<>(),
                tempMap = Converter.deepCopy(pow_cof),
                zero = new HashMap<>() {
                    {
                        put(0, 0);
                    }
                };

        if (that.pow_cof.equals(zero)) {
            if (pow_cof.equals(zero)) return new DivisionResult(zero, zero);
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        if (deg1 < deg2)
            throw new IllegalArgumentException("Cannot divide by larger polynomial.");
        if (pow_cof.get(deg1) % that.pow_cof.get(deg2) != 0)
            throw new IllegalArgumentException("The leading coefficient of the dividend should be divisible by that of the divisor.");

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

    @Override
    public String toString() {
        TreeMap<Integer, Integer> sorted = new TreeMap<>();
        StringJoiner sj = new StringJoiner(" + ");
        sorted.putAll(clean().pow_cof);
        sorted.forEach((k, v) -> {
            StringBuilder sb = new StringBuilder(4);
            if (v != 1 || k == 0)
                sb.append(v);
            if (k != 0) {
                sb.append('x');
                if (k != 1)
                    sb.append('^').append(k);
            }
            sj.add(sb.toString());
        });
        return sj.toString();
    }

    public class DivisionResult {
        private Polynomial quotient;
        private Polynomial remainder;
        
        public DivisionResult(HashMap<Integer, Integer> quotientMap, HashMap<Integer, Integer> remainderMap) {
            this.quotient = new Polynomial(quotientMap).clean();
            this.remainder = new Polynomial(remainderMap).clean();
        }

        public Polynomial getQuotient() {
            return quotient;
        }

        public Polynomial getRemainder() {
            return remainder;
        }
    }

}
