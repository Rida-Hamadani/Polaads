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
        HashMap<Integer, Integer> zero = new HashMap<Integer, Integer>() {
            {
                put(0, 0);
            }
        };
        return pow_cof.equals(zero)
                ? Integer.MIN_VALUE
                : Collections.max(pow_cof.keySet());
    }

    public Polynomial plus(Polynomial that) {
        that.pow_cof.forEach((k, v) -> pow_cof.merge(k, v, (a, b) -> a + b));
        return clean();
    }

    public Polynomial times(Integer scalar) {
        pow_cof.forEach((k, v) -> pow_cof.put(k, scalar * v));
        return clean();
    }

    public Polynomial times(Polynomial that) {
        HashMap<Integer, Integer> product = new HashMap<>();
        that.pow_cof.forEach((k1, v1) -> pow_cof.forEach((k2, v2) -> product.merge(k1 + k2, v1 * v2, (a, b) -> a + b)));
        return setMap(product).clean();
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

}
