package com.polynomials;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberTheory {
    public static Integer gcd(ArrayList<Integer> nums) {
        AtomicInteger n = new AtomicInteger(nums.get(0));
        nums.forEach(m -> n.set(gcd(n.get(), m)));
        return n.get();
    }

    public static int gcd(int a, int b) {
        int c;
        while (b != 0) {
            c = b;
            b = a % b;
            a = c;
        }
        return a;
    }
}
