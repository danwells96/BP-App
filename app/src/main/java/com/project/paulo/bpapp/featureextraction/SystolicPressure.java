package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMax;
import com.project.paulo.bpapp.mathematics.Diff;
import com.project.paulo.bpapp.mathematics.FindIndex;

public class SystolicPressure {

    public static double getSystolicPressure(double[] beat) {
        //Sets default value to first value of beat array
        double systolicPressure = beat[0];


        //Obtains maximum value and index from given beat
        double maxValue = ArrayMax.getArrayMax(beat);
        int maxIndex = FindIndex.findIndex(beat, maxValue);

        //Calculates average of max value and surrounding values based off maxIndex position
        if(maxIndex == beat.length-1){
            systolicPressure = (beat[maxIndex] + beat[maxIndex-1] + beat[maxIndex-2])/3;
        }else if(maxIndex == 0){
            systolicPressure = (beat[maxIndex] + beat[maxIndex+1] + beat[maxIndex+2])/3;
        }else{
            systolicPressure = (beat[maxIndex-1] + beat[maxIndex] + beat[maxIndex+1])/3;
        }

        return systolicPressure;
    }

}
