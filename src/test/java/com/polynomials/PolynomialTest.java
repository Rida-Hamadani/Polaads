package com.polynomials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;

public class PolynomialTest {

    Polynomial p1 = null;
    Polynomial p2 = null;
    Polynomial p3 = null;
    Polynomial p4 = null;
    Polynomial p5 = null;

    @BeforeEach
    public void generatePolynomials() {
        HashMap<Integer, Integer> map1 = new HashMap<Integer, Integer>() {
            {
                put(1, 1);
                put(2, 0);
            }
        };
        HashMap<Integer, Integer> map2 = new HashMap<Integer, Integer>() {
            {
                put(2, 1);
            }
        };
        HashMap<Integer, Integer> map3 = new HashMap<Integer, Integer>() {
            {
                put(0, 5);
                put(1, 1);
                put(2, 2);
                put(4, 3);
                put(6, 8);
            }
        };

        p1 = new Polynomial(map1);
        p2 = new Polynomial(map2);
        p3 = new Polynomial(map3);
        p4 = new Polynomial();
        p5 = new Polynomial(new ArrayList<Integer>(Arrays.asList(0)), new ArrayList<Integer>(Arrays.asList(1)));
    }

    @Test
    public void testDegree() {
        assertEquals(p1.getDegree().intValue(), 1);
        assertEquals(p2.getDegree().intValue(), 2);
        assertEquals(p3.getDegree().intValue(), 6);
        assertEquals(p4.getDegree().intValue(), Integer.MIN_VALUE);
        assertEquals(p5.getDegree().intValue(), 0);
    }

    @Test
    public void testAddition() {
        HashMap<Integer, Integer> resMap1 = new HashMap<Integer, Integer>() {
            {
                put(1, 1);
                put(2, 1);
            }
        };
        HashMap<Integer, Integer> resMap2 = new HashMap<Integer, Integer>() {
            {
                put(0, 5);
                put(1, 2);
                put(2, 3);
                put(4, 3);
                put(6, 8);
            }
        };

        assertEquals(p1.plus(p2).getMap(), resMap1);
        assertEquals(p1.getMap(), p4.plus(p1).getMap());
        assertEquals(p3.plus(p4).getMap(), resMap2);
    }

    @Test
    public void testTimesScalar() {
        HashMap<Integer, Integer> resMap1 = new HashMap<Integer, Integer>() {
            {
                put(1, 10);
            }
        };
        HashMap<Integer, Integer> resMap2 = new HashMap<Integer, Integer>() {
            {
                put(0, 10);
                put(1, 2);
                put(2, 4);
                put(4, 6);
                put(6, 16);
            }
        };
        HashMap<Integer, Integer> resMap3 = new HashMap<Integer, Integer>() {
            {
                put(0, 1);
            }
        };

        p1.times(10);
        p2.times(0);
        p3.times(2);
        p5.times(1);

        assertEquals(p1.getMap(), resMap1);
        assertEquals(p2.getMap(), p4.getMap());
        assertEquals(p3.getMap(), resMap2);
        assertEquals(p5.getMap(), resMap3);
    }

    @Test
    public void testTimesPolynomial() {
        HashMap<Integer, Integer> resMap1 = new HashMap<Integer, Integer>() {
            {
                put(2, 5);
                put(3, 1);
                put(4, 2);
                put(6, 3);
                put(8, 8);
            }
        };
        p1.times(p4);
        p3.times(p2);

        assertEquals(p1.getMap(), p4.getMap());
        assertEquals(p3.getMap(), resMap1);
        assertEquals(p3.times(p5).getMap(), resMap1);
    }

    @Test
    public void testToString() {
        assertEquals(p1.toString(), "x");
        assertEquals(p2.toString(), "x^2");
        assertEquals(p3.toString(), "5 + x + 2x^2 + 3x^4 + 8x^6");
        assertEquals(p4.toString(), "0");
        assertEquals(p5.toString(), "1");
    }
}
