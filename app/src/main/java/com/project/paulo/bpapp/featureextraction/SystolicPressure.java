package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMax;
import com.project.paulo.bpapp.mathematics.Diff;
import com.project.paulo.bpapp.mathematics.FindIndex;

public class SystolicPressure {

    public static double getSystolicPressure(double[] beat) {
        double systolicPressure = beat[0];

        //double[] derivative = Diff.diff(beat);

        double maxValue = ArrayMax.getArrayMax(beat);
        int maxIndex = FindIndex.findIndex(beat, maxValue);

        if(maxIndex == beat.length-1){
            systolicPressure = (beat[maxIndex] + beat[maxIndex-1] + beat[maxIndex-2])/3;
        }else if(maxIndex == 0){
            systolicPressure = (beat[maxIndex] + beat[maxIndex+1] + beat[maxIndex+2])/3;
        }else{
            systolicPressure = (beat[maxIndex-1] + beat[maxIndex] + beat[maxIndex+1])/3;
        }

        /*for(int i = FindIndex.findIndex(derivative, maxDerivative); i < derivative.length - 1; i++){
            if(derivative[i] >= 0 && derivative[i+1] < 0){
                systolicPressure = beat[i];
                break;
            }
        }*/

        return systolicPressure;
    }

}
