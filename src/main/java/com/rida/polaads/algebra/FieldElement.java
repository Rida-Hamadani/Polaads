package com.rida.polaads.algebra;

public interface FieldElement extends UfdElement {
    FieldElement multiplicativeInverse();

    default FieldElement divide (FieldElement that) {
        return (FieldElement) multiply(that.multiplicativeInverse());
    }
}
