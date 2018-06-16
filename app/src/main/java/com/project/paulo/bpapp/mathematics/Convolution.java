package com.project.paulo.bpapp.mathematics;

public class Convolution {

    public static double[] conv(double[] a, double[] b) {
        double[] c;
        int na = a.length;
        int nb = b.length;
        if (na > nb) {
            if (nb > 1) {
                c = new double[na+nb-1];
                for (int i = 0; i < c.length; i++) {
                    if (i < a.length) {
                        c[i] = a[i];
                    } else {
                        c[i] = 0.0;
                    }
                }
                a = c;
            }
            c = Filter.filter(b, new double [] {1.0} , a);
        } else {
            if (na > 1) {
                c = new double[na+nb-1];
                for (int i = 0; i < c.length; i++) {
                    if (i < b.length) {
                        c[i] = b[i];
                    } else {
                        c[i] = 0.0;
                    }
                }
                b = c;
            }
            c = Filter.filter(a, new double [] {1.0}, b);
        }
        return c;
    }

}
