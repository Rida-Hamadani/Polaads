package com.rida.polaads.algebra;

public interface Ring<T extends RingElement> {
    T getAdditiveNeutral();
    T getMultiplicativeNeutral();
}
