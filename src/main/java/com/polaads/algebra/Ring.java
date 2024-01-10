package com.polaads.algebra;

public abstract class Ring {
    private static Ring additiveNeutral;

    private static Ring multiplicativeNeutral;

    public static <T extends Ring> T getAdditiveNeutral() {
        return (T) additiveNeutral;
    }

    public static <T extends Ring> T getMultiplicativeNeutral() {
        return (T) multiplicativeNeutral;
    }

    public abstract <T extends Ring> T add(T that);

    public abstract <T extends Ring> T multiply(T that);

    public abstract <T extends Ring> T additiveInverse();

    public <T extends Ring> T subtract(T that) {
        return add(that.additiveInverse());
    }
}
