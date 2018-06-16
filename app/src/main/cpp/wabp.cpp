/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 *
 * wabp.cpp
 *
 * Code generation for function 'wabp'
 *
 */

/* Include files */
#include "wabp.h"
#include "sum.h"
#include "sum.cpp"
#include "conv.h"
#include "conv.cpp"
#include "diff.h"
#include "diff.cpp"
#include "filter.h"
#include "filter.cpp"
#include <jni.h>

extern "C"
JNIEXPORT jdoubleArray

JNICALL
Java_com_project_paulo_bpapp_Graph_wabp(
        JNIEnv *env, jobject /* this */,
        const double abp[100], double onsets_data[], int onsets_size[1]) {

    int i;
    static const double dv0[11] = { 1.0, 0.0, 0.0, 0.0, 0.0, -2.0, 0.0, 0.0, 0.0,
                                    0.0, 1.0 };

    static const double dv1[3] = { 1.0, -2.0, 1.0 };

    double b_abp[100];
    double z[100];
    double b_z[97];
    double dypos[96];
    double dv2[111];
    double avg0;
    double ssf[113];
    double Threshold0;
    double lockout;
    double timer;
    double counter;
    int t;
    int idx;
    double mtmp;
    double b_mtmp;
    static const signed char iv0[17] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                                         13, 14, 15, 16 };

    static const signed char iv1[17] = { -16, -15, -14, -13, -12, -11, -10, -9, -8,
                                         -7, -6, -5, -4, -3, -2, -1, 0 };

    int tt_size_idx_1;
    signed char tt_data[63];
    double tmp_data[63];
    boolean_T x_data[63];
    int k;
    boolean_T exitg1;
    signed char ii_data[1];
    unsigned char BeatTime_data[1];

    /*  WABP  ABP waveform onset detector. */
    /*    onsets = WABP(ABP) obtains the onset time (in samples)  */
    /*        of each beat in the ABP waveform. */
    /*  */
    /*    In:   ABP (125Hz sampled) */
    /*    Out:  Onset sample time */
    /*   */
    /*    Usage: */
    /*    - ABP waveform must have units of mmHg */
    /*  */
    /*    Written by James Sun (xinsun@mit.edu) on Nov 19, 2005.  This ABP onset */
    /*    detector is adapted from Dr. Wei Zong's wabp.c. */
    /*  Input checks */
    /* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */
    /*  scale physiologic ABP */
    /*  LPF */
    /*  Takes care of 4 sample group delay */
    /*  Slope-sum function */
    for (i = 0; i < 100; i++) {
        b_abp[i] = abp[i] * 20.0 - 1600.0;
    }

    filter(dv0, dv1, b_abp, z);
    for (i = 0; i < 97; i++) {
        b_z[i] = ((z[i + 3] / 24.0 + 30.0) + 1600.0) / 20.0;
    }

    diff(b_z, dypos);
    for (i = 0; i < 96; i++) {
        avg0 = dypos[i];
        if (dypos[i] < 0.0) {
            avg0 = 0.0;
        }

        dypos[i] = avg0;
    }

    conv(dypos, dv2);
    ssf[0] = 0.0;
    ssf[1] = 0.0;
    memcpy(&ssf[2], &dv2[0], 111U * sizeof(double));

    /*  Decision rule */
    avg0 = sum(*(double (*)[199])&ssf[0]) / 199.0;

    /*  average of 1st 8 seconds (1000 samples) of SSF */
    Threshold0 = 3.0 * avg0;

    /*  initial decision threshold */
    /*  ignoring "learning period" for now */
    lockout = 0.0;

    /*  lockout >0 means we are in refractory */
    timer = 0.0;
    memset(&z[0], 0, 100U * sizeof(double));
    counter = 0.0;
    for (t = 0; t < 47; t++) {
        lockout--;
        timer++;

        /*  Timer used for counting time after previous ABP pulse */
        if ((lockout < 1.0) && (ssf[t + 49] > avg0 + 5.0)) {
            /*  Not in refractory and SSF has exceeded threshold here */
            timer = 0.0;
            mtmp = ssf[t + 49];

            /*  Find local max of SSF */
            b_mtmp = ssf[t + 33];
            for (idx = 0; idx < 16; idx++) {
                if (ssf[(iv0[idx + 1] + t) + 49] > mtmp) {
                    mtmp = ssf[(idx + t) + 50];
                }

                if (ssf[(iv1[idx + 1] + t) + 49] < b_mtmp) {
                    b_mtmp = ssf[(idx + t) + 34];
                }
            }

            /*  Find local min of SSF */
            if (mtmp > b_mtmp + 10.0) {
                avg0 = 0.01 * mtmp;

                /*  Onset is at the time in which local SSF just exceeds 0.01*maxSSF */
                if (50 + t < t + 34) {
                    tt_size_idx_1 = 0;
                } else {
                    tt_size_idx_1 = 17;
                    for (i = 0; i < 17; i++) {
                        tt_data[i] = (signed char)((t + i) + 34);
                    }
                }

                for (i = 0; i < tt_size_idx_1; i++) {
                    tmp_data[i] = ssf[tt_data[i] - 2];
                }

                for (i = 0; i < tt_size_idx_1; i++) {
                    x_data[i] = (ssf[tt_data[i] - 1] - tmp_data[i] < avg0);
                }

                if (1 < tt_size_idx_1) {
                    k = 1;
                } else {
                    k = tt_size_idx_1;
                }

                idx = 0;
                exitg1 = false;
                while ((!exitg1) && (tt_size_idx_1 > 0)) {
                    if (x_data[tt_size_idx_1 - 1]) {
                        idx = 1;
                        ii_data[0] = (signed char)tt_size_idx_1;
                        exitg1 = true;
                    } else {
                        tt_size_idx_1--;
                    }
                }

                if (k == 1) {
                    if (idx == 0) {
                        k = 0;
                    }
                } else {
                    k = !(1 > idx);
                }

                for (i = 0; i < k; i++) {
                    BeatTime_data[i] = (unsigned char)((ii_data[i] + t) + 33);
                }

                counter++;
                if (k == 0) {
                    counter--;
                } else {
                    z[(int)counter - 1] = BeatTime_data[0];
                }

                Threshold0 += 0.1 * (mtmp - Threshold0);

                /*  adjust threshold */
                avg0 = Threshold0 / 3.0;

                /*  adjust avg */
                lockout = 32.0;

                /*  lock so prevent sensing right after detection (refractory period) */
            }
        }

        if (timer > 312.0) {
            /*  Lower threshold if no pulse detection for a while */
            Threshold0--;
            avg0 = Threshold0 / 3.0;
        }
    }

    idx = 0;
    for (i = 0; i < 100; i++) {
        if (z[i] != 0.0) {
            idx++;
        }
    }

    onsets_size[0] = idx;
    idx = 0;
    for (i = 0; i < 100; i++) {
        if (z[i] != 0.0) {
            onsets_data[idx] = z[i] - 2.0;
            idx++;
        }
    }

};

// ORIGINAL C++ FUNCTION

///* Function Definitions */
//void wabp(const double abp[100], double onsets_data[], int onsets_size[1])
//{
//    int i;
//    static const double dv0[11] = { 1.0, 0.0, 0.0, 0.0, 0.0, -2.0, 0.0, 0.0, 0.0,
//                                    0.0, 1.0 };
//
//    static const double dv1[3] = { 1.0, -2.0, 1.0 };
//
//    double b_abp[100];
//    double z[100];
//    double b_z[97];
//    double dypos[96];
//    double dv2[111];
//    double avg0;
//    double ssf[113];
//    double Threshold0;
//    double lockout;
//    double timer;
//    double counter;
//    int t;
//    int idx;
//    double mtmp;
//    double b_mtmp;
//    static const signed char iv0[17] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//                                         13, 14, 15, 16 };
//
//    static const signed char iv1[17] = { -16, -15, -14, -13, -12, -11, -10, -9, -8,
//                                         -7, -6, -5, -4, -3, -2, -1, 0 };
//
//    int tt_size_idx_1;
//    signed char tt_data[63];
//    double tmp_data[63];
//    boolean_T x_data[63];
//    int k;
//    boolean_T exitg1;
//    signed char ii_data[1];
//    unsigned char BeatTime_data[1];
//
//    /*  WABP  ABP waveform onset detector. */
//    /*    onsets = WABP(ABP) obtains the onset time (in samples)  */
//    /*        of each beat in the ABP waveform. */
//    /*  */
//    /*    In:   ABP (125Hz sampled) */
//    /*    Out:  Onset sample time */
//    /*   */
//    /*    Usage: */
//    /*    - ABP waveform must have units of mmHg */
//    /*  */
//    /*    Written by James Sun (xinsun@mit.edu) on Nov 19, 2005.  This ABP onset */
//    /*    detector is adapted from Dr. Wei Zong's wabp.c. */
//    /*  Input checks */
//    /* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */
//    /*  scale physiologic ABP */
//    /*  LPF */
//    /*  Takes care of 4 sample group delay */
//    /*  Slope-sum function */
//    for (i = 0; i < 100; i++) {
//        b_abp[i] = abp[i] * 20.0 - 1600.0;
//    }
//
//    filter(dv0, dv1, b_abp, z);
//    for (i = 0; i < 97; i++) {
//        b_z[i] = ((z[i + 3] / 24.0 + 30.0) + 1600.0) / 20.0;
//    }
//
//    diff(b_z, dypos);
//    for (i = 0; i < 96; i++) {
//        avg0 = dypos[i];
//        if (dypos[i] < 0.0) {
//            avg0 = 0.0;
//        }
//
//        dypos[i] = avg0;
//    }
//
//    conv(dypos, dv2);
//    ssf[0] = 0.0;
//    ssf[1] = 0.0;
//    memcpy(&ssf[2], &dv2[0], 111U * sizeof(double));
//
//    /*  Decision rule */
//    avg0 = sum(*(double (*)[1000])&ssf[0]) / 1000.0;
//
//    /*  average of 1st 8 seconds (1000 samples) of SSF */
//    Threshold0 = 3.0 * avg0;
//
//    /*  initial decision threshold */
//    /*  ignoring "learning period" for now */
//    lockout = 0.0;
//
//    /*  lockout >0 means we are in refractory */
//    timer = 0.0;
//    memset(&z[0], 0, 100U * sizeof(double));
//    counter = 0.0;
//    for (t = 0; t < 47; t++) {
//        lockout--;
//        timer++;
//
//        /*  Timer used for counting time after previous ABP pulse */
//        if ((lockout < 1.0) && (ssf[t + 49] > avg0 + 5.0)) {
//            /*  Not in refractory and SSF has exceeded threshold here */
//            timer = 0.0;
//            mtmp = ssf[t + 49];
//
//            /*  Find local max of SSF */
//            b_mtmp = ssf[t + 33];
//            for (idx = 0; idx < 16; idx++) {
//                if (ssf[(iv0[idx + 1] + t) + 49] > mtmp) {
//                    mtmp = ssf[(idx + t) + 50];
//                }
//
//                if (ssf[(iv1[idx + 1] + t) + 49] < b_mtmp) {
//                    b_mtmp = ssf[(idx + t) + 34];
//                }
//            }
//
//            /*  Find local min of SSF */
//            if (mtmp > b_mtmp + 10.0) {
//                avg0 = 0.01 * mtmp;
//
//                /*  Onset is at the time in which local SSF just exceeds 0.01*maxSSF */
//                if (50 + t < t + 34) {
//                    tt_size_idx_1 = 0;
//                } else {
//                    tt_size_idx_1 = 17;
//                    for (i = 0; i < 17; i++) {
//                        tt_data[i] = (signed char)((t + i) + 34);
//                    }
//                }
//
//                for (i = 0; i < tt_size_idx_1; i++) {
//                    tmp_data[i] = ssf[tt_data[i] - 2];
//                }
//
//                for (i = 0; i < tt_size_idx_1; i++) {
//                    x_data[i] = (ssf[tt_data[i] - 1] - tmp_data[i] < avg0);
//                }
//
//                if (1 < tt_size_idx_1) {
//                    k = 1;
//                } else {
//                    k = tt_size_idx_1;
//                }
//
//                idx = 0;
//                exitg1 = false;
//                while ((!exitg1) && (tt_size_idx_1 > 0)) {
//                    if (x_data[tt_size_idx_1 - 1]) {
//                        idx = 1;
//                        ii_data[0] = (signed char)tt_size_idx_1;
//                        exitg1 = true;
//                    } else {
//                        tt_size_idx_1--;
//                    }
//                }
//
//                if (k == 1) {
//                    if (idx == 0) {
//                        k = 0;
//                    }
//                } else {
//                    k = !(1 > idx);
//                }
//
//                for (i = 0; i < k; i++) {
//                    BeatTime_data[i] = (unsigned char)((ii_data[i] + t) + 33);
//                }
//
//                counter++;
//                if (k == 0) {
//                    counter--;
//                } else {
//                    z[(int)counter - 1] = BeatTime_data[0];
//                }
//
//                Threshold0 += 0.1 * (mtmp - Threshold0);
//
//                /*  adjust threshold */
//                avg0 = Threshold0 / 3.0;
//
//                /*  adjust avg */
//                lockout = 32.0;
//
//                /*  lock so prevent sensing right after detection (refractory period) */
//            }
//        }
//
//        if (timer > 312.0) {
//            /*  Lower threshold if no pulse detection for a while */
//            Threshold0--;
//            avg0 = Threshold0 / 3.0;
//        }
//    }
//
//    idx = 0;
//    for (i = 0; i < 100; i++) {
//        if (z[i] != 0.0) {
//            idx++;
//        }
//    }
//
//    onsets_size[0] = idx;
//    idx = 0;
//    for (i = 0; i < 100; i++) {
//        if (z[i] != 0.0) {
//            onsets_data[idx] = z[i] - 2.0;
//            idx++;
//        }
//    }
//}

/* End of code generation (wabp.cpp) */
