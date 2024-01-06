package com.polynomials;

import java.util.*;

class Matrix {
    private List<List<Integer>> matrix;
    private int p;

    public Matrix() {
        this.matrix = new ArrayList<>();
    }

    public Matrix(List<List<Integer>> matrix) {
        this.matrix = matrix;
    }

    public int getMod() {
        return p;
    }

    public void setMod(int p) {
        this.p = p;
    }

    public List<List<Integer>> get2DList() {
        return matrix;
    }

    public Integer getRowsNumber() {
        return get2DList().size();
    }

    public Integer getColsNumber() {
        return get2DList().isEmpty() ? 0 : get2DList().get(0).size();
    }

    public Integer get(int i, int j) {
        return matrix.get(i).get(j);
    }

    public void set(int i, int j, int element) {
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
                sum.set(i, j, get(i, j) + that.get(i, j));
            }
        }
        return sum;
    }

    // note that all the following subroutines are mod p
    public List<Integer> getMultipliedColumn(int colIndex, int cnt) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < getRowsNumber(); ++i) {
            res.add((cnt * get(i, colIndex)) % getMod());
        }
        return res;
    }

    public void replaceColumn(Integer colIndex, List<Integer> newCol) {
        for (int i = 0; i < getRowsNumber(); ++i) {
            set(i, colIndex, newCol.get(i));
        }
    }

    public void addListToCol(List<Integer> fromCol, Integer toCol) {
        for (int i = 0; i < getRowsNumber(); ++i) {
            set(i, toCol, (fromCol.get(i) + get(i, toCol)) % getMod());
        }
    }

    public Matrix getKernelBasis() {
        Integer n = getColsNumber();

        if (!n.equals(getRowsNumber())) {
            throw new IllegalArgumentException("Must be a square matrix.");
        }

        List<List<Integer>> nullVectors = new ArrayList<>();
        List<Integer> cnts = new ArrayList<>();
        boolean jExists;

        for (int k = 0; k < n; ++k) {
            cnts.add(-1);
        }

        for (int k = 0; k < n; ++k) {
            jExists = false;
            for (int j = 0; j < n; ++j) {
                if (!get(k, j).equals(0) && cnts.get(j) < 0) {
                    replaceColumn(j, getMultipliedColumn(j, -NumberTheory.getModularInverse(get(k, j), getMod())));
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
                List<Integer> nullVector = new ArrayList<>();
                for (int j = 0; j < n; ++j) {
                    Integer vj = 0;
                    if (k == j) {
                        vj = 1;
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
