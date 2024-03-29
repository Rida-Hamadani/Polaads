package com.polaads;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.rida.polaads.utils.Converter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;

public class ConverterTest extends Converter {

    ArrayList<Integer> list1 = null;
    ArrayList<Integer> list2 = null;
    HashMap<Integer, Integer> map = null;

    @BeforeEach
    public void generateData() {
        list1 = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        list2 = new ArrayList<>(Arrays.asList(4, 5, 6, 7));
        map = new HashMap<>(){{
            put(0, 4);
            put(1, 5);
            put(2, 6);
            put(3, 7);
        }};
    }

    @Test
    public void testConnect() {
        assertEquals(Converter.connect(list1, list2), map);
    }

    @Test
    public void testDeepCopy() {
        assertEquals(map, Converter.deepCopy(map));
    }
}
