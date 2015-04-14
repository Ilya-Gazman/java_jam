package com.gazman.math;

import java.math.BigInteger;

/**
 * Created by ilyagazman on 4/9/15.
 */
public class MathUtils {

    public static BigInteger factorial(int value){
        if(value < 0){
            throw new IllegalArgumentException("Value must be positive");
        }

        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= value; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }

        return result;
    }


}
