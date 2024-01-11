package com.polaads;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import com.rida.polaads.arithmetic.NumberTheory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class NumberTheoryTest extends NumberTheory {
    ArrayList<Integer> arr1 = null;
    ArrayList<Integer> arr2 = null;
    ArrayList<Integer> arr3 = null;
    ArrayList<Integer> arr4 = null;

    @BeforeEach
    public void createData() {
        arr1 = new ArrayList<>(Arrays.asList(16, 12, 44, 28));
        arr2 = new ArrayList<>(Arrays.asList(1));
        arr3 = new ArrayList<>(Arrays.asList(42, 63));
        arr4 = new ArrayList<>(Arrays.asList(0, 5, 10));
    }

    @Test
    public void testGcd() {
        assertEquals(gcd(arr1), 4);
        assertEquals(gcd(arr2), 1);
        assertEquals(gcd(arr3), 21);
        assertEquals(gcd(arr4), 5);
        
    }

    @Test
    public void testGetModularInverse() {
        int a = -2;
        assertEquals(getModularInverse(a, 13), 6);
        assertEquals(-2, a);
    }
}
