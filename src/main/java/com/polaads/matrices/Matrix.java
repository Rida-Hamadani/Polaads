package com.polaads.matrices;

import com.polaads.algebra.Field;

import java.util.*;

public class Matrix<T extends Field> {
    private List<List<T>> matrix;

    public Matrix() {
        this.matrix = new ArrayList<>();
    }

    public Matrix(List<List<T>> matrix) {
        this.matrix = matrix;
    }


    public List<List<T>> get2DList() {
        return matrix;
    }

    public Integer getRowsNumber() {
        return get2DList().size();
    }

    public Integer getColsNumber() {
        return get2DList().isEmpty() ? 0 : get2DList().get(0).size();
    }

    public T get(int i, int j) {
        return matrix.get(i).get(j);
    }

    public void set(int i, int j, T element) {
        matrix.get(i).remove(j);
        matrix.get(i).add(j, element);
    }

    public Boolean isEmpty() {
        return get2DList().isEmpty();
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
                sum.set(i, j, (Field) get(i, j).add(that.get(i, j)));
            }
        }
        return sum;
    }

    public List<T> getMultipliedColumn(int colIndex, T cnt) {
        List<T> res = new ArrayList<>();
        for (int i = 0; i < getRowsNumber(); ++i) {
            res.add((T) cnt.multiply(get(i, colIndex)));
        }
        return res;
    }

    public void replaceColumn(int colIndex, List<T> newCol) {
        for (int i = 0; i < getRowsNumber(); ++i) {
            set(i, colIndex, newCol.get(i));
        }
    }

    public void addListToCol(List<T> fromCol, int toCol) {
        for (int i = 0; i < getRowsNumber(); ++i) {
            set(i, toCol, fromCol.get(i).add(get(i, toCol)));
        }
    }

    public Matrix getKernelBasis() {
        Integer n = getColsNumber();

        if (!n.equals(getRowsNumber())) {
            throw new IllegalArgumentException("Must be a square matrix.");
        }

        List<List<T>> nullVectors = new ArrayList<>();
        List<Integer> cnts = new ArrayList<>();
        boolean jExists;

        for (int k = 0; k < n; ++k) {
            cnts.add(-1);
        }

        for (int k = 0; k < n; ++k) {
            jExists = false;
            for (int j = 0; j < n; ++j) {
                if (!get(k, j).equals(T.getAdditiveNeutral()) && cnts.get(j) < 0) {
                    replaceColumn(j, getMultipliedColumn(j, (get(k, j).multiplicativeInverse()).additiveInverse()));
                    for (int i = 0; i < n; ++i) {
                        if (i == j) continue;
                        addListToCol(getMultipliedColumn(j, get(k, i)), i);
                    }
                    cnts.set(j, k);
                    jExists = true;
                    break;
                }
            }
            if (!jExists) {
                List<T> nullVector = new ArrayList<>();
                for (int j = 0; j < n; ++j) {
                    T vj = T.getAdditiveNeutral();
                    if (k == j) {
                        vj = T.getMultiplicativeNeutral();
                    }
                    if (cnts.contains(j)) {
                        vj = get(k, cnts.indexOf(j));
                    }
                    nullVector.add(vj);
                }
                nullVectors.add(nullVector);
            }
        }

        return new Matrix(nullVectors);
    }
}
