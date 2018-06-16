/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 *
 * wabp.h
 *
 * Code generation for function 'wabp'
 *
 */

#ifndef BPAPP_WABP_H
#define BPAPP_WABP_H

/* Include files */
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include "rtwtypes.h"
#include "wabp_types.h"
#include <jni.h>

extern "C"
JNIEXPORT jdoubleArray

JNICALL Java_com_project_paulo_bpapp_Graph_wabp(
        JNIEnv *env, jobject /* this */,
        const double abp[100], double onsets_data[], int onsets_size[1]);

// ORIGINAL C++ FUNCTION DECLARATION

///* Function Declarations */
//extern void wabp(const double abp[100], double onsets_data[], int onsets_size[1]);

#endif //BPAPP_WABP_H

/* End of code generation (wabp.h) */
