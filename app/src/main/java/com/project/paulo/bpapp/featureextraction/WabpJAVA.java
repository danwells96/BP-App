package com.project.paulo.bpapp.featureextraction;

import com.project.paulo.bpapp.mathematics.Convolution;
import com.project.paulo.bpapp.mathematics.Diff;
import com.project.paulo.bpapp.mathematics.Filter;

import java.util.ArrayList;
import java.util.Arrays;

public class WabpJAVA {

    public static int[] wabpJAVA(double[] abp) {
        double offset = 1600.0;
        double scale = 20.0;
        double[] Araw = abp;

        for (int i = 0; i < Araw.length; i++) {
            Araw[i] = Araw[i] * scale - offset;
        }

        double[] b = {1, 0, 0, 0, 0, -2, 0, 0, 0, 0, 1};
        double[] a = {1, -2, 1};
        double[] Afiltered = Filter.filter(b, a, Araw);

        for (int i = 0; i < Afiltered.length; i++) {
            Afiltered[i] = Afiltered[i] / 24 + 30;
        }

        double[] A = new double[Afiltered.length - 3];
        System.arraycopy(Afiltered, 3, A, 0, Afiltered.length - 3);

        for (int i = 0; i < A.length; i++) {
            A[i] = (A[i] + offset) / scale;
        }

        double[] dypos = Diff.diff(A);

        for (int i = 0; i < dypos.length; i++) {
            if(dypos[i] < 0.0) {
                dypos[i] = 0.0;
            }
        }

        a = new double[16];
        Arrays.fill(a, 1);

        double[] convResult = Convolution.conv(a, dypos);

        double[] ssf = new double[convResult.length + 2];
        ssf[0] = 0.0;
        ssf[1] = 0.0;
        System.arraycopy(convResult, 0, ssf, 2, convResult.length);

        int avg0 = 0;

        // Based on first 5 seconds of ssf sampled at 25ms
        for (int i = 0; i < 199; i++) {
            avg0 += ssf[i];
        }

        avg0 /= 199;
        int Threshold0 = 3*avg0;
        int lockout = 0;
        int timer = 0;
        int[] zRaw = new int[abp.length];
        int counter = 0;

        for (int t = 49; t < ssf.length - 18; t++) {
            lockout -= 1;
            timer += 1;

            if (lockout < 1 && ssf[t] > avg0 + 5) {
                timer = 0;

                double maxSSF = ssf[t];
                for (int i = t; i < t + 17; i++) {
                    if (ssf[i] > maxSSF) {
                        maxSSF = ssf[i];
                    }
                }

                double minSSF = ssf[t - 16];
                for (int i = t - 16; i < t + 1; i++) {
                    if (ssf[i] < minSSF) {
                        minSSF = ssf[i];
                    }
                }

                if (maxSSF > minSSF + 10) {
                    double onset = 0.01 * maxSSF;

                    double[] dssf = new double[17];
                    for (int i = t - 16; i < t + 1; i++) {
                        dssf[i + 16 - t] = ssf[i] - ssf[i - 1];
                    }

                    int BeatTime = -1;
                    for (int i = 0; i < dssf.length; i++) {
                        if (dssf[i] < onset) {
                            BeatTime = i;
                        }
                    }

                    BeatTime = BeatTime + t - 17;
                    counter += 1;

                    if (BeatTime < 0) {
                        counter -= 1;
                    } else {
                        zRaw[counter - 1] = BeatTime;
                    }

                    Threshold0 += 0.1 * (maxSSF - Threshold0);
                    avg0 = Threshold0 / 3;
                    lockout = 32;

                }
            }

            if (timer > 312) {
                Threshold0 -= 1;
                avg0 = Threshold0 / 3;
            }
        }

        ArrayList<Integer> zList = new ArrayList<>();
        for (int i = 0; i < zRaw.length; i++) {
            if (zRaw[i] > 0) {
                zList.add(zRaw[i] - 2);
            }
        }

        int[] z = new int[zList.size()];
        for(int i = 0; i < zList.size(); i++)
        {
            z[i] = zList.get(i);
        }

        return z;
    }

}
