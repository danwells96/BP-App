package com.project.paulo.bpapp.mathematics;

public class Filter {

    public static double[] filter(double[] b, double[] a, double[] x) {
        double[] filter;
        double[] a1 = ArrayDivision.getRealArrayScalarDiv(a, a[0]);
        double[] b1 = ArrayDivision.getRealArrayScalarDiv(b, a[0]);
        int sx = x.length;
        filter = new double[sx];
        filter[0] = b1[0]*x[0];
        for (int i = 1; i < sx; i++) {
            filter[i] = 0.0;
            for (int j = 0; j <= i; j++) {
                int k = i-j;
                if (j > 0) {
                    if ((k < b1.length) && (j < x.length)) {
                        filter[i] += b1[k]*x[j];
                    }
                    if ((k < filter.length) && (j < a1.length)) {
                        filter[i] -= a1[j]*filter[k];
                    }
                } else {
                    if ((k < b1.length) && (j < x.length)) {
                        filter[i] += (b1[k]*x[j]);
                    }
                }
            }
        }
        return filter;
    }

}
