package com.polynomials;
import java.util.*;

public class Polynomial {
    private HashMap<Integer, Integer> pow_cof; // done this way to support sparse polynomials efficiently

    public Polynomial(HashMap<Integer, Integer> pow_cof) {
        this.pow_cof = pow_cof;
    }

    public Polynomial(ArrayList<Integer> powers, ArrayList<Integer> coefs) {
        this(Converter.connectLists(powers, coefs));
    }

    private Polynomial clean() {
        pow_cof.values().removeIf(v -> v == 0);
        return this;
    }

    public HashMap<Integer, Integer> getMap() {
        return pow_cof;
    }

    public Integer degree() {
        return pow_cof.keySet().size() == 0
                ? Integer.MIN_VALUE
                : Collections.max(clean().pow_cof.keySet());
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
        that.pow_cof.forEach((k1, v1) -> pow_cof.forEach((k2, v2) -> pow_cof.merge(k1 + k2, v1 * v2, (a, b) -> a + b)));
        return clean();
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" + ");
        clean().pow_cof.forEach((k, v) -> {
            StringBuilder sb = new StringBuilder(4);
            sb.append(v);
            if (k != 0) {
                sb.append('x');
                if (k != 1) sb.append('^').append(k);
            }
            sj.add(sb.toString());
        });
        return sj.toString();
    }

}
