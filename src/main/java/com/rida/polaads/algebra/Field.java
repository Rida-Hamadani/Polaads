package com.rida.polaads.algebra;

public abstract class Field extends UFD {
    public abstract <T extends Field> T multiplicativeInverse();

    public <T extends Field> T divide (T that) {
        return multiply(that.multiplicativeInverse());
    }
}
