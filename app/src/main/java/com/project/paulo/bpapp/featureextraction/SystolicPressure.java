package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMax;
import com.project.paulo.bpapp.mathematics.Diff;
import com.project.paulo.bpapp.mathematics.FindIndex;

public class SystolicPressure {

    public static double getSystolicPressure(double[] beat) {
        double systolicPressure = beat[0];

        double[] derivative = Diff.diff(beat);

        double maxDerivative = ArrayMax.getArrayMax(derivative);

        for(int i = FindIndex.findIndex(derivative, maxDerivative); i < derivative.length - 1; i++){
            if(derivative[i] >= 0 && derivative[i+1] < 0){
                systolicPressure = beat[i];
                break;
            }
        }

        return systolicPressure;
    }

}
