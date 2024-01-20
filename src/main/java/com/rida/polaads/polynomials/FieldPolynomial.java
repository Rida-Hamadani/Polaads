package com.rida.polaads.polynomials;

import com.rida.polaads.algebra.Field;
import com.rida.polaads.algebra.FieldElement;
import com.rida.polaads.utils.Converter;

import java.util.HashMap;
import java.util.Map;

public class FieldPolynomial<T extends FieldElement> extends UfdPolynomial<T>{
    public DivisionResult divide(RingPolynomial that) {
        int deg1 = getDegree(), deg2 = that.getDegree(), quotientDegree = deg1 - deg2;
        Map<Integer, Integer> quotientMap = new HashMap<>(), remainderMap = new HashMap<>(), tempMap = Converter.deepCopy(pow_cof);

        if (pow_cof.equals(Polynomials.ZERO.getMap())) {
            return new DivisionResult(Polynomials.ZERO.getMap(), Polynomials.ZERO.getMap());
        }
        if (that.pow_cof.equals(Polynomials.ZERO.getMap())) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        if (deg1 < deg2) {
            throw new IllegalArgumentException("Cannot divide by larger polynomial.");
        }
        if (pow_cof.get(deg1) % that.pow_cof.get(deg2) != 0) {
            throw new IllegalArgumentException("The leading coefficient of the dividend should be divisible by that of the divisor.");
        }

        for (int i = quotientDegree; i > -1; --i) {
            quotientMap.put(i, tempMap.getOrDefault(i + deg2, 0) / that.pow_cof.getOrDefault(deg2, 0));
            for (int j = deg2 + i - 1; j > i - 1; --j) {
                tempMap.put(j, tempMap.getOrDefault(j, 0) - quotientMap.get(i) * that.pow_cof.getOrDefault(j - i, 0));
            }
        }
        for (int i = 0; i < deg2; ++i) {
            remainderMap.put(i, tempMap.getOrDefault(i, 0));
        }

        return new DivisionResult(quotientMap, remainderMap);
    }
}
