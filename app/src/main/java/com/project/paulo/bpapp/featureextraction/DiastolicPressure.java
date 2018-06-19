package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMin;
import com.project.paulo.bpapp.mathematics.FindIndex;

import java.util.Arrays;

public class DiastolicPressure {

    public static double getDiastolicPressure(double[] beat, double systolicPressure) {
        int systolicIndex = FindIndex.findIndex(beat, systolicPressure);

        return ArrayMin.getArrayMin(Arrays.copyOfRange(beat, systolicIndex, beat.length));
    }

}
