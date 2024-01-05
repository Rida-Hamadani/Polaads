package com.polynomials;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Converter {
    public static Map<Integer, Integer> connect(ArrayList<Integer> domain, ArrayList<Integer> range) {
        return new HashMap<Integer, Integer>(IntStream.range(0, domain.size())
                .boxed()
                .collect(Collectors.toMap(domain::get, range::get)));
    }

    public static Map<Integer, Integer> deepCopy(Map<Integer, Integer> mapToCopy) {
        Map<Integer, Integer> newMap = new HashMap<>();
        mapToCopy.forEach((k, v) -> newMap.put(Integer.valueOf(k), Integer.valueOf(v)));
        return newMap;
    }

    public static List<List<Integer>> vectorToMatrix(int rowSize, int... elements) {
        List<List<Integer>> matrix = new ArrayList<>();
        int count = 0;
        List<Integer> row = null;
        while (count < elements.length) {
            if (count % rowSize == 0) {
                row = new ArrayList<>();
            }
            row.add(elements[count]);
            if (count++ % rowSize == 0) {
                matrix.add(row);
            }
        }
        return matrix;
    }
}
