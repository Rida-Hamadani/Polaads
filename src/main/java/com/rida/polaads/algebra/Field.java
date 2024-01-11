package com.rida.polaads.algebra;

public abstract class Field extends Ring {
    public abstract <T extends Field> T multiplicativeInverse();

    public <T extends Field> T divide (T that) {
        return multiply(that.multiplicativeInverse());
    }
}
