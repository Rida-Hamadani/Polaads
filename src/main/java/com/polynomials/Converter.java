package com.polynomials;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Converter {
    public static HashMap<Integer, Integer> connectLists(ArrayList<Integer> domain, ArrayList<Integer> range) {
        return new HashMap<Integer, Integer>(IntStream.range(0, domain.size())
                .boxed()
                .collect(Collectors.toMap(domain::get, range::get)));
    }
}
