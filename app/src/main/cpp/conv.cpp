/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 *
 * conv.cpp
 *
 * Code generation for function 'conv'
 *
 */

/* Include files */
#include "wabp.h"
#include "conv.h"

/* Function Definitions */
void conv(const double B[96], double C[111])
{
    int k;
    int b_k;
    memset(&C[0], 0, 111U * sizeof(double));
    for (k = 0; k < 16; k++) {
        for (b_k = 0; b_k < 96; b_k++) {
            C[k + b_k] += B[b_k];
        }
    }
}

/* End of code generation (conv.cpp) */
