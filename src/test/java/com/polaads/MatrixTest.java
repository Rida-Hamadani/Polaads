package com.polaads;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.polaads.matrices.Matrix;
import com.polaads.utils.Converter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;

public class MatrixTest {

    private Matrix m1 = null;
    private Matrix m2 = null;
    private Matrix m3 = null;
//    @BeforeEach
//    public void generateMatrices() {
//        m1 = new Matrix(Converter.vectorToMatrix(8,
//                0, 0, 0, 0, 0, 0, 0, 0,
//                2, 0, 7, 11, 10, 12, 5, 11,
//                3, 6, 3, 3, 0, 4, 7, 2,
//                4, 3, 6, 4, 1, 6, 2, 3,
//                2, 11, 8, 8, 2, 1, 3, 11,
//                6, 11, 8, 6, 2, 6, 10, 9,
//                5, 11, 7, 10, 0, 11, 6, 12,
//                3, 3, 12, 5, 0, 11, 9, 11));
//        m1.setMod(13);
//    }
//
//    @Test
//    public void testNullSpace() {
//        List<List<Integer>> res = Converter.vectorToMatrix(8,
//                1, 0, 0, 0, 0, 0, 0, 0,
//                0, 5, 5, 0, 9, 5, 1, 0,
//                0, 9, 11, 9, 10, 12, 0, 1);
//        Matrix mat = m1.getKernelBasis();
//        for (int i = 0; i < res.size(); ++i) {
//            for (int j = 0; j < res.get(0).size(); ++j) {
//                assertEquals(Math.floorMod(res.get(i).get(j), 13), Math.floorMod(mat.get(i, j), 13));
//            }
//        }
//    }
}
