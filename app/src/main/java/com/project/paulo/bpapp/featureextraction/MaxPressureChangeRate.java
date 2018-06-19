package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.FindIndex;

public class MaxPressureChangeRate {

    public static double getMaxPressureChangeRate(double[] beat, double systolicPressure, int arsr){
        return (systolicPressure - beat[1])/((FindIndex.findIndex(beat, systolicPressure) + 1)/(1000.0/arsr));
    }

}
