package com.project.paulo.bpapp.mathematics;

public class FindIndex {

    public static int findIndex(double[] array, double value) {
        int index = -1;

        for(int i = 0; i < array.length; i++){
            if(array[i] == value){
                index = i;
                break;
            }
        }

        if(index < 0){
            index = 1;
        }

        return index;
    }

}
