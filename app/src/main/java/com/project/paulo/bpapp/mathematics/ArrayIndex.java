package com.project.paulo.bpapp.mathematics;

public class ArrayIndex {

    public static int getArrayIndex(double[] array, double value) {
        int index = 0;

        for(int i = 0 ; i < array.length; i++){

            if(Double.compare(array[i], value) == 0){
                index = i;
                break;
            }
        }
        return index;
    }

}
