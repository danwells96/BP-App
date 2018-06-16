package com.project.paulo.bpapp.mathematics;

public class ArraySum {

    public static double getArraySum(double[] array) {
        double sum = 0;

        for(double i : array) {
            sum += i;
        }
        return sum;
    }

}
