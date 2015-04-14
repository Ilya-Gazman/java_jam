package com.gazman.math;

import java.math.BigInteger;

/**
 * Created by ilyagazman on 4/9/15.
 */
public class Combination {

    private int n, r;
    private boolean considerOrder, allowRepetition;


    /**
     * How many different balls can be selected
     * @param balls balls count
     */
    public Combination setTypesToChooseFrom(int balls) {
        n = balls;
        return this;
    }

    /**
     * How many balls do you select
     * @param ballsSelected
     * @return
     */
    public Combination setNumberChosen(int ballsSelected) {
        r = ballsSelected;
        return this;
    }

    public Combination setAllowRepetition(boolean allowRepetition) {
        this.allowRepetition = allowRepetition;
        return this;
    }

    public Combination setConsiderOrder(boolean considerOrder) {
        this.considerOrder = considerOrder;
        return this;
    }

    public BigInteger execute(){
        if(r > n){
            throw new IllegalArgumentException("r > n");
        }
        if(considerOrder && allowRepetition){
            return BigInteger.valueOf(n).pow(r);
        }
        if(considerOrder && !allowRepetition){
            BigInteger nFactorial = MathUtils.factorial(n);
            BigInteger deltaFactorial = MathUtils.factorial(n - r);
            return nFactorial.divide(deltaFactorial);
        }
        if(!considerOrder && allowRepetition){
            BigInteger a = MathUtils.factorial(n + r - 1);
            BigInteger b = MathUtils.factorial(n - 1).multiply(MathUtils.factorial(r));
            return a.divide(b);
        }
        BigInteger a = MathUtils.factorial(n);
        BigInteger b = MathUtils.factorial(n - r).multiply(MathUtils.factorial(r));
        return a.divide(b);
    }
}
