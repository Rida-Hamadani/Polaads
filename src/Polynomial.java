import java.util.*;

public class Polynomial {
    private HashMap<Integer, Integer> pow_cof; // done this way to support sparse polynomials efficiently

    private Polynomial clean() {
        pow_cof.values().removeIf(v -> v == 0);
        return this;
    }

    public Integer degree() {
        return Collections.max(clean().pow_cof.keySet());
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

}
