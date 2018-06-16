package com.project.paulo.bpapp.mathematics;

public class ArrayMax {

    public static double getArrayMax(double[] array) {
        double max = array[0];
        for(int i = 1; i < array.length; i++){
            if(array[i] > max){
                max = array[i];
            }
        }
        return max;
    }

}
