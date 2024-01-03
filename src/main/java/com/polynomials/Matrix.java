package com.polynomials;

import java.util.*;

class Matrix {
    private List<List<Integer>> matrix;

    public Matrix() {
        this.matrix = new ArrayList<>();
    }

    public List<List<Integer>> get2DArray() {
        return matrix;
    }

    public Integer getRowsNumber() {
        return get2DArray().size();
    }

    public Integer getColsNumber() {
        return get2DArray().isEmpty() ? 0 : get2DArray().get(0).size();
    }

    public Integer get(int i, int j) {
        return matrix.get(i).get(j);
    }

    public void set(int i, int j, int element) {
        matrix.get(i).add(j, element);
    }

    public Boolean isEmpty() {
        return get2DArray().isEmpty();
    }

    public Matrix add(Matrix that) {
        if (!getRowsNumber().equals(that.getRowsNumber()) || !getColsNumber().equals(that.getColsNumber())) {
            throw new IllegalArgumentException("Matrices must have same dimensions.");
        }
        if(isEmpty() || that.isEmpty()) {
            return new Matrix();
        }

        Matrix sum = new Matrix();
        for(int i = 0; i < getRowsNumber(); ++i) {
            for(int j = 0; j < getColsNumber(); ++j) {
                sum.set(i, j, get(i, j) + that.get(i, j));
            }
        }
        return sum;
    }
}
