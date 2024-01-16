package com.rida.polaads.algebra;

import com.rida.polaads.utils.SetTheory;

import java.util.*;

public interface UfdElement extends RingElement { // unique factorization domain
    List<UfdElement> factorize();

    static UfdElement defactorize(List<UfdElement> factoredElement) {
        if (factoredElement.size() == 0) {
            return null;
        }
        UfdElement res = (UfdElement) factoredElement.get(0).getMultiplicativeNeutral();

        for (UfdElement element : factoredElement) {
            res = (UfdElement) res.multiply(element);
        }

        return res;
    }

    default List<UfdElement> gcd(UfdElement x, UfdElement y) {
        return SetTheory.intersection(x.factorize(), y.factorize());
    }

    default List<UfdElement> gcd(List<UfdElement> elements) {
        List<List<UfdElement>> gcds = new ArrayList<>();

        for (UfdElement element : elements) {
            gcds.add(element.factorize());
        }

        return SetTheory.intersection(gcds);
    }
}
