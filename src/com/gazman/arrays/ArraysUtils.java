package com.gazman.arrays;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ilyagazman on 3/12/15.
 */
public class ArraysUtils {

    public static ArrayList<Integer> mergeArrays(int[] group1, int[] group2){
        ArrayList<Integer> options = new ArrayList<>();
        Arrays.sort(group1);
        Arrays.sort(group2);

        int index1 = 0, index2 = 0;

        while (true) {
            if(index1 >= group1.length || index2 >= group2.length){
                return options;
            }

            if(group1[index1] == group2[index2]){
                options.add(group1[index1]);
                index1++;
                index2++;
            }

            else if(group1[index1] < group2[index2]){
                int result = Arrays.binarySearch(group1, index1 + 1, group1.length, group2[index2]);
                if(result < 0){
                    index1 = -result - 1;
                }
                else {
                    index1 = result;
                }
            }
            else if(group2[index2] < group1[index1]){
                int result = Arrays.binarySearch(group2, index2 + 1, group2.length, group1[index1]);
                if(result < 0){
                    index2 = -result - 1;
                }
                else {
                    index2 = result;
                }
            }
        }
    }

    public static long sum(int[] anInt) {
        long sum = 0;
        for (int i : anInt) {
            sum += i;
        }
        return sum;
    }
}
