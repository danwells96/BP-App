package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMax;
import com.project.paulo.bpapp.mathematics.FindIndex;

import java.util.Arrays;

public class DicroticPeak {

    public static double getDicroticPeak(double[] beat, double dicroticNotchPressure){
        int dicroticNotchIndex = FindIndex.findIndex(beat, dicroticNotchPressure);

        return ArrayMax.getArrayMax(Arrays.copyOfRange(beat, dicroticNotchIndex, beat.length));
    }

}
