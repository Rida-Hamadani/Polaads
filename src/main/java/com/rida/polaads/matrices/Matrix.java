package com.rida.polaads.matrices;

import com.rida.polaads.algebra.Field;
import com.rida.polaads.algebra.FieldElement;

import java.util.*;

public class Matrix<T extends FieldElement> {
    private List<List<T>> matrix;
    private Field<T> field;

    public Matrix() {
        this.matrix = new ArrayList<>();
    }

    public Matrix(List<List<T>> matrix) {
        if (matrix.isEmpty() || matrix.get(0).isEmpty()) {
            throw new IllegalArgumentException("Matrix is empty");
        }
        this.matrix = matrix;
        this.field = (Field<T>) matrix.get(0).get(0).getSet();
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
                sum.set(i, j, (FieldElement) get(i, j).add(that.get(i, j)));
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
            set(i, toCol, (T) fromCol.get(i).add(get(i, toCol)));
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
                if (!get(k, j).equals(field.getAdditiveNeutral()) && cnts.get(j) < 0) {
                    replaceColumn(j, getMultipliedColumn(j, (T) (get(k, j).multiplicativeInverse()).getAdditiveInverse()));
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
                    T vj = field.getAdditiveNeutral();
                    if (k == j) {
                        vj = field.getMultiplicativeNeutral();
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
