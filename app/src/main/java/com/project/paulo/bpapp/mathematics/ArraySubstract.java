package com.project.paulo.bpapp.mathematics;

public class ArraySubstract {

    public static double[] getArraySubstraction(double[] array1, double[] array2) {
        double[] array3 = new double[array1.length];

        for(int i = 0; i < array1.length; i++){
            array3[i] = array1[i] - array2[i];
        }

        return array3;
    }

}
