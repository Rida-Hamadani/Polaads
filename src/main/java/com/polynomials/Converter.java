package com.polynomials;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Converter {
    public static HashMap<Integer, Integer> connect(ArrayList<Integer> domain, ArrayList<Integer> range) {
        return new HashMap<Integer, Integer>(IntStream.range(0, domain.size())
                .boxed()
                .collect(Collectors.toMap(domain::get, range::get)));
    }

    public static HashMap<Integer, Integer> deepCopy(HashMap<Integer, Integer> mapToCopy) {
        return (HashMap<Integer, Integer>) mapToCopy.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }
}
