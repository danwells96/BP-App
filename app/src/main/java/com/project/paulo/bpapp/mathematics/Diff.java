package com.project.paulo.bpapp.mathematics;

public class Diff {

    public static double[] diff(double[] X) {
        double[] difference = new double[X.length - 1];

        for (int i = 0; i < difference.length; i++) {
            difference[i] = X[i+1] - X[i];
        }

        return difference;
    }

}
