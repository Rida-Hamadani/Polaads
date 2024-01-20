package com.rida.polaads.algebra;

import com.rida.polaads.utils.SetTheory;

import java.util.ArrayList;
import java.util.List;

public interface UFD<T extends UfdElement> extends Ring<T> {
    default List<T> gcd(T x, T y) {
        return (List<T>) SetTheory.intersection(x.factorize(), y.factorize());
    }

    default List<T> gcd(List<T> elements) {
        List<List<T>> gcds = new ArrayList<>();

        for (UfdElement element : elements) {
            gcds.add((List<T>) element.factorize());
        }

        return SetTheory.intersection(gcds);
    }
}
