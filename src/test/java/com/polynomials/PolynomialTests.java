package com.polynomials;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.*;

public class PolynomialTests {

    Polynomial p1 = null;
    Polynomial p2 = null;

    @Before
    public void generatePolynomials() {
        HashMap<Integer, Integer> map1 = new HashMap<Integer, Integer>() {
            {
                put(1, 1);
            }
        };
        HashMap<Integer, Integer> map2 = new HashMap<Integer, Integer>() {
            {
                put(2, 1);
            }
        };
        p1 = new Polynomial(map1);
        p2 = new Polynomial(map2);
    }

    @Test
    public void TestAddition() {
        p1.plus(p2);
        HashMap<Integer, Integer> resMap = new HashMap<Integer, Integer>() {
            {
                put(1, 1);
                put(2, 1);
            }
        };
        assertEquals(p1.getMap(), resMap);
    }

}
