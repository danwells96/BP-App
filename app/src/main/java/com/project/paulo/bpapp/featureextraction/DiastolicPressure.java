package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMin;
import com.project.paulo.bpapp.mathematics.FindIndex;

import java.util.Arrays;

public class DiastolicPressure {

    public static double getDiastolicPressure(double[] beat, double systolicPressure) {
        double diastolicPressure = beat[0];
        int systolicIndex = FindIndex.findIndex(beat, systolicPressure);
        double minValue = ArrayMin.getArrayMin(Arrays.copyOfRange(beat, systolicIndex, beat.length));
        int minIndex = FindIndex.findIndex(beat, minValue);

        if(minIndex == beat.length-1){
            diastolicPressure = (beat[minIndex] + beat[minIndex-1] + beat[minIndex-2])/3;
        }else if(minIndex == 0){
            diastolicPressure = (beat[minIndex] + beat[minIndex+1] + beat[minIndex+2])/3;
        }else{
            diastolicPressure = (beat[minIndex-1] + beat[minIndex] + beat[minIndex+1])/3;
        }

        return diastolicPressure;
    }

}
