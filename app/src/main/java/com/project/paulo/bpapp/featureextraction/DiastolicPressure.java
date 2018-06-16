package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.ArrayMin;

public class DiastolicPressure {

    public static double getDiastolicPressure(double[] beat) {
        return ArrayMin.getArrayMin(beat);
    }

}
