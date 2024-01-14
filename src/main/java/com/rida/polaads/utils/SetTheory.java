package com.rida.polaads.utils;

import java.util.*;

public class SetTheory {
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> inter = new ArrayList<>();

        for (T t : list1) {
            if(list2.contains(t)) {
                inter.add(t);
            }
        }

        return inter;
    }

    public static <T> List<T> intersection(List<List<T>> lists) {
        List<T> inter = new ArrayList<>();

        if (lists.size() == 0) {
            return inter;
        }

        for (List<T> list : lists) {
            inter = intersection(inter, list);
        }

        return inter;
    }
}
