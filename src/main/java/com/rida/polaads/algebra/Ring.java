package com.rida.polaads.algebra;

public abstract class Ring<T> {
    private T additiveNeutral;

    private T multiplicativeNeutral;

    public T getAdditiveNeutral() {
        return additiveNeutral;
    }

    public T getMultiplicativeNeutral() {
        return multiplicativeNeutral;
    }

    public abstract T add(T x, T y);

    public abstract T multiply(T x, T y);

    public abstract T getAdditiveInverse(T x);

    public T subtract(T x, T y) {
        return add(x, getAdditiveInverse(y));
    }
}
