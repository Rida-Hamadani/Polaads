package com.rida.polaads.algebra;

import com.rida.polaads.utils.SetTheory;

import java.util.*;

public abstract class UFD<T> extends Ring<T> { // unique factorization domain
    public abstract List<T> factorize(T t);

    public T defactorize(List<T> factoredElement) {
        T res = getMultiplicativeNeutral();

        for (T element : factoredElement) {
            res = multiply(res, element);
        }

        return res;
    }

    public List<T> gcd(T x, T y) {
        return SetTheory.intersection(factorize(x), factorize(y));
    }

    public List<T> gcd(List<T> elements) {
        List<List<T>> gcds = new ArrayList<>();

        for (T element : elements) {
            gcds.add(factorize(element));
        }

        return SetTheory.intersection(gcds);
    }
}
