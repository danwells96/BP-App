package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMin;
import com.project.paulo.bpapp.mathematics.FindIndex;

import java.util.Arrays;

public class DiastolicPressure {

    public static double getDiastolicPressure(double[] beat, double systolicPressure) {
        //Sets default value to first value of array beat
        double diastolicPressure = beat[0];
        //Finds index of systolic pressure to limit possible diastolic values
        int systolicIndex = FindIndex.findIndex(beat, systolicPressure);
        //Finds minimum value and index from given beat after systolic index
        double minValue = ArrayMin.getArrayMin(Arrays.copyOfRange(beat, systolicIndex, beat.length));
        int minIndex = FindIndex.findIndex(beat, minValue);

        //Calculates average of min value and surrounding values based off minIndex position
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
