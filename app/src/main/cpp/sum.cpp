/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 *
 * sum.cpp
 *
 * Code generation for function 'sum'
 *
 */

/* Include files */
#include "wabp.h"
#include "sum.h"

/* Function Definitions */
double sum(const double x[1000])
{
    double y;
    int k;
    y = x[0];
    for (k = 0; k < 999; k++) {
        y += x[k + 1];
    }

    return y;
}

/* End of code generation (sum.cpp) */
