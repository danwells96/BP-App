/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 *
 * filter.cpp
 *
 * Code generation for function 'filter'
 *
 */

/* Include files */
#include "wabp.h"
#include "filter.h"

/* Function Definitions */
void filter(const double b[11], const double a[3], const double x[100], double
y[100])
{
    int k;
    int naxpy;
    int j;
    double as;
    memset(&y[0], 0, 100U * sizeof(double));
    for (k = 0; k < 100; k++) {
        naxpy = 100 - k;
        if (!(naxpy < 11)) {
            naxpy = 11;
        }

        for (j = 0; j + 1 <= naxpy; j++) {
            y[k + j] += x[k] * b[j];
        }

        naxpy = 99 - k;
        if (!(naxpy < 2)) {
            naxpy = 2;
        }

        as = -y[k];
        for (j = 1; j <= naxpy; j++) {
            y[k + j] += as * a[j];
        }
    }
}

/* End of code generation (filter.cpp) */
