/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 *
 * diff.cpp
 *
 * Code generation for function 'diff'
 *
 */

/* Include files */
#include "wabp.h"
#include "diff.h"

/* Function Definitions */
void diff(const double x[97], double y[96])
{
    int ixLead;
    int iyLead;
    double work;
    int m;
    double tmp1;
    ixLead = 1;
    iyLead = 0;
    work = x[0];
    for (m = 0; m < 96; m++) {
        tmp1 = work;
        work = x[ixLead];
        tmp1 = x[ixLead] - tmp1;
        ixLead++;
        y[iyLead] = tmp1;
        iyLead++;
    }
}

/* End of code generation (diff.cpp) */
