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
}
