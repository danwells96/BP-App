package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMin;
import com.project.paulo.bpapp.mathematics.Diff;
import com.project.paulo.bpapp.mathematics.FindIndex;

import java.util.Arrays;

public class DicroticNotch {

    public static double getDicroticNotch(double[] beat, double systolicPressure){
        double dicroticNotchPressure = beat[0];

        double[] derivative = Diff.diff(beat);

        double minDerivative = ArrayMin.getArrayMin(Arrays.copyOfRange(derivative, FindIndex.findIndex(beat, systolicPressure),derivative.length));

        for(int i = FindIndex.findIndex(derivative, minDerivative); i < derivative.length - 1; i++){
            if(derivative[i] < 0 && derivative[i+1] >= 0){
                dicroticNotchPressure = beat[i];
                break;
            }
        }

        return dicroticNotchPressure;
    }

}
