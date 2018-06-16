package com.project.paulo.bpapp.mathematics;

public class ArrayMin {

    public static double getArrayMin(double[] array){
        double min = array[0];
        for(int i = 1; i < array.length; i++){
            if(array[i] < min){
                min = array[i];
            }
        }
        return min;
    }

}
