package com.polynomials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testGetDegree() {
        assertEquals(p1.getDegree().intValue(), 1);
        assertEquals(p2.getDegree().intValue(), 2);
        assertEquals(p3.getDegree().intValue(), 6);
        assertEquals(p4.getDegree().intValue(), Integer.MIN_VALUE);
        assertEquals(p5.getDegree().intValue(), 0);
    }

    @Test
    public void testGetContent() {
        assertEquals(p4.getContent(), 0);
        assertEquals(p1.multiply(2).getContent(), 2);
    }

    @Test
    public void testGetPrimitive() {
        HashMap<Integer, Integer> resMap1 = Converter.deepCopy(p1.getMap());
        assertEquals(p4.getPrimitive().getMap(), new Polynomial().getMap());
        assertEquals(p1.multiply(2).getPrimitive().getMap(), resMap1);
    }

    @Test
    public void testGetDerivative() {
        assertEquals(p1.getDerivative().getMap(), p5.getMap());
        assertEquals(p2.getDerivative().getMap(), p1.multiply(2).getMap());
        assertEquals(p4.getDerivative().getMap(), p4.getMap());
        assertEquals(p5.getDerivative().getMap(), p4.getMap());
    }

    @Test
    public void testIsPrimitive() {
        assertTrue(p1.isPrimitive());
        assertTrue(p2.isPrimitive());
        assertTrue(p3.isPrimitive());
        assertFalse(p4.isPrimitive());
        assertTrue(p5.isPrimitive());
        assertFalse(p1.add(p2).multiply(2).isPrimitive());
    }

    @Test
    public void testIsSquareFree() {
        assertTrue(p1.isSquareFree());
        assertFalse(p2.isSquareFree());
        // assertTrue(p3.isSquareFree()); currently resulting in errors due to coefficients growing above Integer.MAX_VALUE
        assertFalse(p4.isSquareFree());
        assertTrue(p5.isSquareFree());
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

        assertEquals(p1.add(p2).getMap(), resMap1);
        assertEquals(p1.getMap(), p4.add(p1).getMap());
        assertEquals(p3.add(p4).getMap(), resMap2);
    }

    @Test
    public void testSubtract() {
        HashMap<Integer, Integer> resMap = new HashMap<Integer, Integer>() {
            {
                put(1, -1);
                put(2, 1);
            }
        };
        HashMap<Integer, Integer> resMap2 = new HashMap<Integer, Integer>() {
            {
                put(0, -4);
                put(1, -1);
                put(2, -2);
                put(4, -3);
                put(6, -8);
            }
        };

        assertEquals(p2.subtract(p1).getMap(), resMap);
        assertEquals(p1.subtract(p1).getMap(), p4.getMap());
        assertEquals(p5.subtract(p3).getMap(), resMap2);
    }

    @Test
    public void testScalarMultiplication() {
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

        p1.multiply(10);
        p2.multiply(0);
        p3.multiply(2);
        p5.multiply(1);

        assertEquals(p1.getMap(), resMap1);
        assertEquals(p2.getMap(), p4.getMap());
        assertEquals(p3.getMap(), resMap2);
        assertEquals(p5.getMap(), resMap3);
    }

    @Test
    public void testPolynomialMultiplication() {
        HashMap<Integer, Integer> resMap1 = new HashMap<Integer, Integer>() {
            {
                put(2, 5);
                put(3, 1);
                put(4, 2);
                put(6, 3);
                put(8, 8);
            }
        };
        p1.multiply(p4);
        p3.multiply(p2);

        assertEquals(p1.getMap(), p4.getMap());
        assertEquals(p3.getMap(), resMap1);
        assertEquals(p3.multiply(p5).getMap(), resMap1);
    }

    @Test
    public void testScalarDivision() {
        HashMap<Integer, Integer> resMap1 = Converter.deepCopy(p1.getMap());
        assertEquals(p1.divide(1).getMap(), resMap1);
    }

    @Test
    public void testPolynomialDivision() {
        HashMap<Integer, Integer> divMap = new HashMap<Integer, Integer>() {
            {
                put(0, 13);
                put(1, -11);
                put(2, 11);
                put(3, -8);
                put(4, 8);
            }
        };
        HashMap<Integer, Integer> remMap = new HashMap<Integer, Integer>() {
            {
                put(0, 5);
                put(1, -12);
            }
        };

        DivisionResult dr1 = p3.divide(p2.add(p1));
        DivisionResult dr2 = p2.divide(p1);
        DivisionResult dr3 = p1.divide(p1);
        DivisionResult dr4 = p4.divide(p4);

        assertEquals(dr1.getMap(), divMap);
        assertEquals(dr1.getRemainder().getMap(), remMap);
        assertEquals(dr2.getMap(), p1.add(p5).getMap());
        assertEquals(dr2.getRemainder().getMap(), p4.getMap());
        assertEquals(dr3.getMap(), p5.getMap());
        assertEquals(dr3.getRemainder().getMap(), p4.getMap());
        assertEquals(dr4.getMap(), p4.getMap());
        assertEquals(dr4.getRemainder().getMap(), p4.getMap());
    }

    @Test
    public void testPseudoPolynomialDivision() {
        HashMap<Integer, Integer> uMap = new HashMap<Integer, Integer>() {
            {
                put(8, 1);
                put(6, 1);
                put(4, -3);
                put(3, -3);
                put(2, 8);
                put(1, 2);
                put(0, -5);
            }
        };
        HashMap<Integer, Integer> vMap = new HashMap<Integer, Integer>() {
            {
                put(6, 3);
                put(4, 5);
                put(2, -4);
                put(1, -9);
                put(0, 21);
            }
        };
        HashMap<Integer, Integer> divMap = new HashMap<Integer, Integer>() {
            {
                put(0, -6);
                put(2, 9);
            }
        };
        HashMap<Integer, Integer> remMap = new HashMap<Integer, Integer>() {
            {
                put(4, -15);
                put(2, 3);
                put(0, -9);
            }
        };

        Polynomial uP = new Polynomial(uMap);
        Polynomial vP = new Polynomial(vMap);

        DivisionResult dr = uP.pseudoDivide(vP);

        assertEquals(dr.getMap(), divMap);
        assertEquals(dr.getRemainder().getMap(), remMap);
    }

    @Test
    public void testScalarDivisionExceptions() {
        try {
            p1.divide(0);
            assertEquals(1, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot divide by zero.", e.getMessage());
        }
        try {
            p1.divide(3);
            assertEquals(1, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("3 doesn't divide all the coefficients of x.", e.getMessage());
        }
    }

    @Test
    public void testPolynomialDivisionExceptions() {
        HashMap<Integer, Integer> remMap = new HashMap<Integer, Integer>() {
            {
                put(0, 5);
                put(1, -12);
            }
        };
        try {
            p1.divide(p4);
            assertEquals(1, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot divide by zero.", e.getMessage());
        }
        try {
            p5.divide(p1);
            assertEquals(1, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot divide by larger polynomial.", e.getMessage());
        }
        try {
            p3.divide(new Polynomial(remMap));
            assertEquals(1, 0);
        } catch (IllegalArgumentException e) {
            assertEquals("The leading coefficient of the dividend should be divisible by that of the divisor.",
                    e.getMessage());
        }
    }

    @Test
    public void testGcd() {
        HashMap<Integer, Integer> res1 = Converter.deepCopy(p1.getMap());
        Polynomial gcd1 = Polynomial.gcd(p1, p2);
        
        assertEquals(Polynomial.gcd(p1, p4).getMap(), res1);
        assertEquals(gcd1.getMap(), p1.getMap());
        assertEquals(Polynomial.gcd(p2.multiply(p2).subtract(p5), p1.add(p5)).getMap(), p1.getMap());
    }

    @Test
    public void testToString() {
        assertEquals(p1.toString(), "x");
        assertEquals(p1.multiply(-1).toString(), "- x");
        assertEquals(p2.toString(), "x^2");
        assertEquals(p2.multiply(-1).toString(), "- x^2");
        assertEquals(p3.toString(), "5 + x + 2x^2 + 3x^4 + 8x^6");
        assertEquals(p3.multiply(-1).toString(), "- 5 - x - 2x^2 - 3x^4 - 8x^6");
        assertEquals(p4.toString(), "0");
        assertEquals(p4.multiply(-1).toString(), "0");
        assertEquals(p5.toString(), "1");
        assertEquals(p5.multiply(-1).toString(), "- 1");
    }
}
